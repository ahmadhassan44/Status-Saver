package com.example.livesaver.home.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.livesaver.R
import com.example.livesaver.home.domain.MediaModel
import com.example.livesaver.home.presentation.activities.PermissionRequester
import com.example.livesaver.home.presentation.adapters.MediaAdapter
import com.example.livesaver.home.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.ArrayList

@AndroidEntryPoint
class ImagesFragment : Fragment() {
    private lateinit var imagesScreen: View
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var permissionRequester: PermissionRequester? = null
    private lateinit var swipeToRefreshLayout: SwipeRefreshLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PermissionRequester) {
            permissionRequester = context
        } else {
            throw RuntimeException("$context must implement PermissionRequester")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        imagesScreen = inflater.inflate(R.layout.fragment_images, container, false)
        return imagesScreen
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            homeViewModel.noPermissionState.observe(viewLifecycleOwner) { noPermission ->
                Log.d("Permission","permission state:$noPermission")
                if (noPermission) {
                    imagesScreen.findViewById<View>(R.id.nopermissionsview).visibility =
                        View.VISIBLE
                    imagesScreen.findViewById<RecyclerView>(R.id.refresh).visibility =
                        View.GONE
                } else {
                    swipeToRefreshLayout=imagesScreen.findViewById(R.id.refresh)
                    swipeToRefreshLayout.setOnRefreshListener {
                        homeViewModel.refreshRepository()
                        swipeToRefreshLayout.isRefreshing=false
                    }
                    homeViewModel.refreshRepository()
                    imagesScreen.findViewById<View>(R.id.nopermissionsview).visibility = View.GONE
                    val recView=imagesScreen.findViewById<RecyclerView>(R.id.imagesrecview)
                    recView.layoutManager=GridLayoutManager(requireActivity(),3)
                    val adapter=MediaAdapter(
                        ArrayList<MediaModel>()
                    )
                    homeViewModel.imageStatuses.observe(viewLifecycleOwner, Observer { statuses ->
                        statuses?.let {
                            Log.d("Permission", "Updating adapter with list of size: ${it.size}")
                            adapter.updateList(it)
                            if(it.isEmpty()){
                                imagesScreen.findViewById<View>(R.id.noImagesView).visibility=View.VISIBLE
                            }
                            else{
                                imagesScreen.findViewById<View>(R.id.noImagesView).visibility=View.GONE
                            }
                            Log.d("ImagesFragment", "Adapter updated with list of size: ${it.size}")
                        }
                    })
                    recView.adapter=adapter
                }
            }
        }

        imagesScreen.findViewById<Button>(R.id.button).setOnClickListener {
            permissionRequester?.requestStoragePermission()
        }
    }

    override fun onDetach() {
        super.onDetach()
        permissionRequester = null
    }
}
