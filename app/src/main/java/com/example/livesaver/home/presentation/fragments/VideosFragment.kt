package com.example.livesaver.home.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.livesaver.R

class VideosFragment : Fragment() {
    private lateinit var videosScreen:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        videosScreen=inflater.inflate(R.layout.fragment_videos, container, false)
        return videosScreen
    }

}