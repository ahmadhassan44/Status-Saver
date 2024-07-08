package com.example.livesaver.home.presentation.fragments

import android.content.Context
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
        lifecycleScope.launch {
            homeViewModel.noPermissionState.observe(viewLifecycleOwner) {
                if (it) {
                    videosScreen.findViewById<View>(R.id.nopermissionsview).visibility =
                        View.VISIBLE
                    videosScreen.findViewById<RecyclerView>(R.id.videosrecview).visibility =
                        View.GONE
                } else {
                    videosScreen.findViewById<View>(R.id.nopermissionsview).visibility = View.GONE
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
                                videosScreen.findViewById<View>(R.id.noImagesView).visibility =
                                    View.VISIBLE
                            } else {
                                videosScreen.findViewById<View>(R.id.noImagesView).visibility =
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