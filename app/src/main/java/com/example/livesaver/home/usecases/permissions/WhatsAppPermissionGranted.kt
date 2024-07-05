package com.example.livesaver.home.usecases.permissions

import com.example.livesaver.app.domain.LocalUserManager

class WhatsAppPermissionGranted(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke() {
        localUserManager.whatsApppermissionGranted()
    }
}