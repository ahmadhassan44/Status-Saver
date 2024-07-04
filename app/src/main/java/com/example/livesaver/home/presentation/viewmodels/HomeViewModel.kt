package com.example.livesaver.home.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livesaver.app.domain.AppMode
import com.example.livesaver.app.usecases.AppModeUsecases
import com.example.livesaver.app.usecases.ChangeAppMode
import com.example.livesaver.app.usecases.ReadAppMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appModeUsecases: AppModeUsecases
):ViewModel() {
    private val _isChecked1 = MutableLiveData<Boolean>()
    val isChecked1: MutableLiveData<Boolean> = _isChecked1

    private val _isChecked2 = MutableLiveData<Boolean>()
    val isChecked2: MutableLiveData<Boolean> = _isChecked2

    private val _isVisable = MutableLiveData<Boolean>()
    val isVisable: MutableLiveData<Boolean> = _isVisable

    private val _noPermissionState = MutableLiveData<Boolean>()
    val noPermissionState: MutableLiveData<Boolean> = _noPermissionState

    private val _appModeState= MutableLiveData<AppMode>()
    val appModeState: MutableLiveData<AppMode> = _appModeState

    init {
        _isChecked1.value = false
        _isChecked2.value = false
        isVisable.value = false
        _appModeState.value= AppMode.WHATSAPP
        viewModelScope.launch {
            appModeUsecases.readAppMode.invoke().collect{
                _appModeState.value=it
            }
        }
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
        }
    }
}