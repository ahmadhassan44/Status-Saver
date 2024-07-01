package com.example.livesaver.onboarding

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(

):ViewModel() {
    var onBoardingState= mutableStateOf(0)
    
}