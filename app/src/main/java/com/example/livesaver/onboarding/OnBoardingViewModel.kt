package com.example.livesaver.onboarding

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livesaver.onboarding.usescase.appentry.AppEntryUsecases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val appEntryUsecases: AppEntryUsecases

): ViewModel() {
    private val _currentScreen = MutableLiveData<Int>()
    val currentScreen: LiveData<Int> get() = _currentScreen
    private val _progress=MutableLiveData<Int>()
    val progress:LiveData<Int> get()=_progress
    init {
        _currentScreen.value = 1 // Start at the first screen
        _progress.value=0
    }
    private fun moveToNextScreen() {
        _currentScreen.value = _currentScreen.value?.plus(1)
    }
    fun startProgressBar() {
        viewModelScope.launch {
            for (i in 1..100) {
                delay(10) // Delay of 10ms for each increment, total of 1 second
                _progress.value = i
            }
            moveToNextScreen()
        }
    }
    fun onEvent(event: OnBoardingEvent) {
        when (event) {
            is OnBoardingEvent.GetStartedClicked -> {
                moveToNextScreen()
            }
            is OnBoardingEvent.SaveAppEntry -> {
                viewModelScope.launch {
                    appEntryUsecases.saveAppEntry()
                }
            }
        }
    }
}