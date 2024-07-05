package com.example.livesaver.home.usecases.folderUri

import android.net.Uri
import com.example.livesaver.app.domain.LocalUserManager

class SaveWhatsappFolderUri(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(uri:Uri) {
        localUserManager.savewhatsappfolderuri(uri)
    }
}