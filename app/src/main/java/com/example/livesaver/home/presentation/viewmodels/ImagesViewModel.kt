package com.example.livesaver.home.presentation.viewmodels


import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.livesaver.home.data.Repository
import com.example.livesaver.home.domain.MediaModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var _imagesList:MutableLiveData<ArrayList<MediaModel>> =MutableLiveData()
    val imagesList:MutableLiveData<ArrayList<MediaModel>>
        get() = _imagesList

    fun fetchImages(activity: Activity){

    }
}
