package com.example.livesaver.home.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.livesaver.R
import com.example.livesaver.home.permissionsUsecases.PermissionsUsecases
import com.example.livesaver.home.presentation.activities.PermissionRequester
import com.example.livesaver.home.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class ImagesFragment : Fragment() {
    private lateinit var imagesScreen:View
    private val homeViewModel:HomeViewModel by activityViewModels()
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
        imagesScreen=inflater.inflate(R.layout.fragment_images, container, false)
        return imagesScreen
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            homeViewModel.noPermissionState.observe(viewLifecycleOwner) {
                if(it){
                    imagesScreen.findViewById<View>(R.id.nopermissionsview).visibility=View.VISIBLE
                }
                else
                    imagesScreen.findViewById<View>(R.id.nopermissionsview).visibility=View.GONE
            }
        }
        imagesScreen.findViewById<View>(R.id.nopermissionsview).findViewById<Button>(R.id.button).setOnClickListener {
            permissionRequester?.requestStoragePermission()
        }
    }
    override fun onDetach() {
        super.onDetach()
        permissionRequester = null
    }
}