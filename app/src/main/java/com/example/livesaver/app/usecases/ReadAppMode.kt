package com.example.livesaver.app.usecases

import com.example.livesaver.app.domain.AppMode
import com.example.livesaver.app.domain.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadAppMode(
    private val localUserManager: LocalUserManager
) {
    operator fun invoke():Flow<AppMode> {
        return localUserManager.readAppMode()
    }
}