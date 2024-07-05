package com.example.livesaver.app.domain

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface LocalUserManager {
    suspend fun saveAppEntry()
    fun readAppEntry(): Flow<Boolean>
    suspend fun changeAppMode(newmode:AppMode)
    fun readAppMode(): Flow<AppMode>
    fun readWhatsappPermission():Flow<Boolean>
    fun readWhatsappBusinessPermission():Flow<Boolean>
    suspend fun whatsApppermissionGranted()
    suspend fun whatsAppBusinesspermissionGranted()
    suspend fun savewhatsappfolderuri(uri:Uri)
    suspend fun savewhatsappbusinessfolderuri(uri:Uri)
    fun readwhatsappfolderuri():Flow<String>
    fun readwhatsappbusinessfolderuri():Flow<String>


}