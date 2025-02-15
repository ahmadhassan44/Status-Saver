package com.example.livesaver.onboarding.usescases.appentry

import com.example.livesaver.app.domain.LocalUserManager

class SaveAppEntry(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){
        localUserManager.saveAppEntry()
    }
}