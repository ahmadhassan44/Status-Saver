package com.example.livesaver.home.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

):ViewModel() {
    private val _isChecked1 = MutableLiveData<Boolean>()
    val isChecked1: MutableLiveData<Boolean> = _isChecked1
    private val _isChecked2 = MutableLiveData<Boolean>()
    val isChecked2: MutableLiveData<Boolean> = _isChecked2
    private val _isVisable = MutableLiveData<Boolean>()
    val isVisable: MutableLiveData<Boolean> = _isVisable
    init {
        _isChecked1.value = false
        _isChecked2.value = false
        isVisable.value = false
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

    fun onAllowButtonClicked() {
        val storageChecked = _isChecked1.value ?: false
        val notificationChecked = _isChecked2.value ?: false
        when {
            storageChecked && notificationChecked -> {

            }
            storageChecked -> {
                // Request storage permission
            }
            notificationChecked -> {
                // Request notification permission
            }
        }
    }
}