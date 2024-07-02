package com.example.livesaver.onboarding.usescase.appentry

import android.util.Log
import com.example.livesaver.onboarding.domain.LocalUserManager

class SaveAppEntry(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){
        localUserManager.saveAppEntry()
    }
}