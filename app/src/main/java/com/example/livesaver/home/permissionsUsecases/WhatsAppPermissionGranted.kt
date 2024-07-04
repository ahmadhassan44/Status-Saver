package com.example.livesaver.home.permissionsUsecases

import com.example.livesaver.app.domain.LocalUserManager

class WhatsAppPermissionGranted(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke() {
        localUserManager.whatsApppermissionGranted()
    }
}