package com.example.livesaver.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.livesaver.home.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {


}
