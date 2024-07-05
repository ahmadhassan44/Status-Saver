package com.example.livesaver.home.usecases.permissions

import com.example.livesaver.app.domain.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadWhatsAppBusinessPermission(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke(): Flow<Boolean> {
        return localUserManager.readWhatsappBusinessPermission()
    }
}