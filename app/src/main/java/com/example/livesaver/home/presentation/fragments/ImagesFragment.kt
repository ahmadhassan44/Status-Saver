package com.example.livesaver.home.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.livesaver.R
import com.example.livesaver.home.presentation.viewmodels.HomeViewModel

class ImagesFragment : Fragment() {
    private lateinit var imagesScreen:View
    private val homeViewModel:HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        imagesScreen=inflater.inflate(R.layout.fragment_images, container, false)
        return imagesScreen
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeViewModel.appModeState.observe(viewLifecycleOwner){
            Log.d("aht",it.name)
        }
    }
}