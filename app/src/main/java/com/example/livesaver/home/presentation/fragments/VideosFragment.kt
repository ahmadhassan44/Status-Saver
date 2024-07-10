package com.example.livesaver.home.presentation.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.compose.ui.platform.LocalHapticFeedback
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
import kotlinx.coroutines.launch
import java.util.ArrayList

class VideosFragment : Fragment() {
    private lateinit var videosScreen:View
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
        // Inflate the layout for this fragment
        videosScreen=inflater.inflate(R.layout.fragment_videos, container, false)
        return videosScreen
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val noPermissionsView = videosScreen.findViewById<View>(R.id.nopermissionsview)
        val novideosView = videosScreen.findViewById<View>(R.id.noVideosView)
        novideosView.findViewById<Button>(R.id.novideosavailablebtn).setOnClickListener {
            Intent(Intent.ACTION_VIEW).also {
                it.component= ComponentName("com.whatsapp","com.whatsapp.Main")
                try {
                    startActivity(it)
                } catch (e:Exception){
                    Log.e("Error",e.message.toString())
                }
            }
        }
        swipeToRefreshLayout=videosScreen.findViewById(R.id.videoRefresh)
        lifecycleScope.launch {
            homeViewModel.noPermissionState.observe(viewLifecycleOwner) {
                if (it) {
                    noPermissionsView.visibility =
                        View.VISIBLE
                    swipeToRefreshLayout.visibility =
                        View.GONE
                } else {
                    swipeToRefreshLayout.visibility=View.VISIBLE
                    swipeToRefreshLayout=videosScreen.findViewById(R.id.videoRefresh)
                    swipeToRefreshLayout.setOnRefreshListener {
                        homeViewModel.refreshRepository()
                        swipeToRefreshLayout.isRefreshing=false
                    }
                    noPermissionsView.visibility = View.GONE
                    val recView = videosScreen.findViewById<RecyclerView>(R.id.videosrecview)
                    recView.layoutManager = GridLayoutManager(requireActivity(), 3)
                    val adapter = MediaAdapter(
                        ArrayList<MediaModel>()
                    )
                    recView.adapter = adapter
                    homeViewModel.videoStatuses.observe(viewLifecycleOwner, Observer { statuses ->
                        statuses?.let {
                            adapter.updateList(it)
                            if (it.isEmpty()) {
                                novideosView.visibility =
                                    View.VISIBLE
                            } else {
                                novideosView.visibility =
                                    View.GONE
                            }
                            Log.d("VideosFragment", "Adapter updated with list of size: ${it.size}")
                        }
                    })
                }
            }
        }
        videosScreen.findViewById<View>(R.id.nopermissionsview).findViewById<Button>(R.id.button).setOnClickListener {
            permissionRequester?.requestStoragePermission()
        }
    }
    override fun onDetach() {
        super.onDetach()
        permissionRequester = null
    }
}