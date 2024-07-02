package com.example.livesaver

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.livesaver.onboarding.usescase.appentry.AppEntryUsecases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEntryUsecases: AppEntryUsecases

):ViewModel() {
    fun readAppEntry(): Flow<Boolean> {
        return appEntryUsecases.readAppEntry()
    }
}