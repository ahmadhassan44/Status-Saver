package com.example.livesaver.home.usecases.permissions

import com.example.livesaver.app.domain.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadWhatsAppPermission(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Boolean> {
        return localUserManager.readWhatsappPermission()
    }
}