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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.livesaver.R
import com.example.livesaver.home.presentation.activities.PermissionRequester
import com.example.livesaver.home.presentation.adapters.MediaAdapter
import com.example.livesaver.home.presentation.viewmodels.HomeViewModel
import com.example.livesaver.home.presentation.viewmodels.ImagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImagesFragment : Fragment() {
    private lateinit var imagesScreen: View
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val imagesViewModel: ImagesViewModel by activityViewModels()
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
        imagesScreen = inflater.inflate(R.layout.fragment_images, container, false)
        return imagesScreen
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            // Observe permission state from HomeViewModel
            homeViewModel.noPermissionState.observe(viewLifecycleOwner) { noPermission ->
                if (noPermission) {
                    imagesScreen.findViewById<View>(R.id.nopermissionsview).visibility = View.VISIBLE
                } else {
                    imagesScreen.findViewById<View>(R.id.nopermissionsview).visibility = View.GONE
                    val recView = imagesScreen.findViewById<RecyclerView>(R.id.imagesrecview)
                    recView.visibility = View.VISIBLE
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

    override fun onResume() {
        super.onResume()

    }
}
