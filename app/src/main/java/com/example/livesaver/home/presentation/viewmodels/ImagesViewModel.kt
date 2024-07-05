package com.example.livesaver.home.presentation.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livesaver.home.data.Repository
import com.example.livesaver.home.domain.MediaModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _imagesLiveData = MutableLiveData<List<MediaModel>>()
    val imagesLiveData: LiveData<List<MediaModel>>
        get() = _imagesLiveData

    fun fetchWhatsappStatuses(activity: Activity) {
        viewModelScope.launch {
            try {
                val fetchedStatuses = repository.fetchWhatsappStatuses(activity)
                val filteredImages = fetchedStatuses.filter { it.mediaType == "image" }
                _imagesLiveData.value = filteredImages
            } catch (e: Exception) {
                Log.e("ImagesViewModel", "Error fetching WhatsApp statuses: ${e.message}")
                // Handle error scenario
            }
        }
    }
}
