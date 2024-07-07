package com.example.livesaver.home.presentation.viewmodels

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livesaver.app.domain.AppMode
import com.example.livesaver.app.usecases.AppModeUsecases
import com.example.livesaver.home.data.Repository
import com.example.livesaver.home.domain.MediaModel
import com.example.livesaver.home.usecases.folderUri.FolderUriUsecases
import com.example.livesaver.home.usecases.permissions.PermissionsUsecases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appModeUsecases: AppModeUsecases,
    private val permissionUsecases: PermissionsUsecases,
    private val folderUriUsecases: FolderUriUsecases,
    private val repository: Repository
) : ViewModel() {
    private val _isChecked1 = MutableLiveData<Boolean>()
    val isChecked1: MutableLiveData<Boolean> = _isChecked1

    private val _isChecked2 = MutableLiveData<Boolean>()
    val isChecked2: MutableLiveData<Boolean> = _isChecked2

    private val _isVisable = MutableLiveData<Boolean>()
    val isVisable: MutableLiveData<Boolean> = _isVisable

    private val _noPermissionState = MutableLiveData<Boolean>()
    val noPermissionState: MutableLiveData<Boolean> = _noPermissionState

    private val _appModeState = MutableLiveData<AppMode>()
    val appModeState: MutableLiveData<AppMode> = _appModeState

    private val _imageStatuses = MutableLiveData<ArrayList<MediaModel>>()
    val imageStatuses: LiveData<ArrayList<MediaModel>> = _imageStatuses

    private val _videoStatuses = MutableLiveData<ArrayList<MediaModel>>()
    val videoStatuses: LiveData<ArrayList<MediaModel>> = _videoStatuses


    init {
        _isChecked1.value = false
        _isChecked2.value = false
        isVisable.value = false
        _appModeState.value = AppMode.WHATSAPP
        viewModelScope.launch {
            appModeUsecases.readAppMode.invoke().collect {
                _appModeState.value = it
            }
        }
        determineAppUiBasedOnModeAndPermission()
        fetchWhatsappImages()
    }

    fun onCheckbox1Toggled(isChecked: Boolean) {
        _isChecked1.value = isChecked
        updateButtonVisibility()
    }

    fun onCheckbox2Toggled(isChecked: Boolean) {
        _isChecked2.value = isChecked
        updateButtonVisibility()
    }

    private fun updateButtonVisibility() {
        isVisable.value = (_isChecked1.value == true || _isChecked2.value == true)
    }

    fun changeAppMode(newMode: AppMode) {
        viewModelScope.launch {
            appModeUsecases.changeAppMode.invoke(newMode)
            determineAppUiBasedOnModeAndPermission()
        }
    }

    fun whatsappPermissionGranted(uri: Uri) {
        viewModelScope.launch {
            permissionUsecases.whatsAppPermissionGranted.invoke()
            determineAppUiBasedOnModeAndPermission()
            folderUriUsecases.whatsappFolderUri.invoke(uri)
        }
    }

    fun whatsappBusinessPermissionGranted(uri: Uri) {
        viewModelScope.launch {
            permissionUsecases.whatsAppBusinessPermissionGranted.invoke()
            determineAppUiBasedOnModeAndPermission()
            folderUriUsecases.whatsappBusinessUri.invoke(uri)
        }
    }

    fun determineAppUiBasedOnModeAndPermission() {
        if (_appModeState.value == AppMode.WHATSAPP) {
            viewModelScope.launch {
                permissionUsecases.readWhatappPermission.invoke().collect {
                    if (it) {
                        _noPermissionState.value = false
                    } else {
                        _noPermissionState.value = true
                    }
                }
            }
        }
    }
    fun fetchWhatsappImages() {
        if (appModeState.value == AppMode.WHATSAPP) {
            viewModelScope.launch {
                val statuses =repository.fetchWhatsappStatuses()
                _imageStatuses.value = ArrayList(statuses.filter { it.mediaType == "image" })
                Log.d("HomeViewModel", "fetchWhatsappImages: ${repository.whatsappStatuses.value}")
            }
        } else {
            viewModelScope.launch {

            }
        }
    }


}