package com.example.livesaver.home.presentation.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
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
        val noPermissionView = imagesScreen.findViewById<View>(R.id.nopermissionsview)
        val noImagesView = imagesScreen.findViewById<View>(R.id.noImagesView)
        val recView = imagesScreen.findViewById<RecyclerView>(R.id.imagesrecview)
        swipeToRefreshLayout=imagesScreen.findViewById(R.id.refresh)
        lifecycleScope.launch {
            homeViewModel.noPermissionState.observe(viewLifecycleOwner) { noPermission ->
                Log.d("Permission"," no permission state:$noPermission")
                if (noPermission) {
                    noPermissionView.visibility = View.VISIBLE
                    recView.visibility=View.GONE
                    Log.d("Permission","no permission")
                } else {
                    noPermissionView.visibility=View.GONE
                    setUpRecyclerView(noPermissionView)
                    Log.d("Permission","setup recyclerview")
                }
            }
        }
        imagesScreen.findViewById<Button>(R.id.button).setOnClickListener {
            permissionRequester?.requestStoragePermission()
        }
        noImagesView.findViewById<Button>(R.id.howtousebtn).setOnClickListener {
            Intent(Intent.ACTION_VIEW).also {
                it.component= ComponentName("com.whatsapp","com.whatsapp.Main")
                try {
                    startActivity(it)
                } catch (e:Exception){
                    Log.e("Error",e.message.toString())
                }
            }
        }
    }
    private fun setUpRecyclerView(noPermissionView: View) {
        noPermissionView.visibility = View.GONE
        swipeToRefreshLayout = imagesScreen.findViewById(R.id.refresh)
        swipeToRefreshLayout.setOnRefreshListener {
            homeViewModel.refreshRepository()
            swipeToRefreshLayout.isRefreshing = false
        }
        val recView = imagesScreen.findViewById<RecyclerView>(R.id.imagesrecview)
        recView.layoutManager = GridLayoutManager(requireActivity(), 3)
        val adapter = MediaAdapter(
            ArrayList<MediaModel>()
        )
        homeViewModel.imageStatuses.observe(viewLifecycleOwner, Observer { statuses ->
            statuses?.let {
                adapter.updateList(it)
                if (it.isEmpty()) {
                    imagesScreen.findViewById<View>(R.id.noImagesView).visibility = View.VISIBLE
                    recView.visibility=View.GONE
                } else {
                    imagesScreen.findViewById<View>(R.id.noImagesView).visibility = View.GONE
                    recView.visibility=View.VISIBLE
                }
            }
        })
        recView.adapter = adapter
    }
    override fun onDetach() {
        super.onDetach()
        permissionRequester = null
    }
}
