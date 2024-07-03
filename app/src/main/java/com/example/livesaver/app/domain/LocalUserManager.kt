package com.example.livesaver.app.domain

import kotlinx.coroutines.flow.Flow

interface LocalUserManager {
    suspend fun saveAppEntry()
    fun readAppEntry(): Flow<Boolean>
    suspend fun chnageAppMode()
    fun readAppMode(): Flow<AppMode>
}