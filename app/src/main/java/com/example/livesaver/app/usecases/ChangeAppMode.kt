package com.example.livesaver.app.usecases

import com.example.livesaver.app.domain.AppMode
import com.example.livesaver.app.domain.LocalUserManager

class ChangeAppMode(
    private val localUserManager: LocalUserManager,
) {
    suspend operator fun invoke(newMode: AppMode) {
        localUserManager.changeAppMode(newMode)
    }
}