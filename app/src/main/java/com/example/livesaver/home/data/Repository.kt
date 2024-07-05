package com.example.livesaver.home.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import com.example.livesaver.app.domain.LocalUserManager
import com.example.livesaver.home.domain.MediaModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localUserManager: LocalUserManager
) {
    val whatsappStatuses = MutableLiveData<ArrayList<MediaModel>>(ArrayList())
    val whatsappBusinessStatuses = MutableLiveData<ArrayList<MediaModel>>(ArrayList())

    suspend fun fetchWhatsappStatuses(activity: Activity): ArrayList<MediaModel> {
        try {
            val folderUriString = localUserManager.readwhatsappfolderuri().first()
            val folderUri = Uri.parse(folderUriString)

            withContext(Dispatchers.Main) {
                try {
                    activity.contentResolver.takePersistableUriPermission(
                        folderUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    Log.e("Repository", "Permission error: ${e.message}")
                }
            }

            val fileDocument = withContext(Dispatchers.IO) {
                DocumentFile.fromTreeUri(context, folderUri)
            }

            if (fileDocument != null && fileDocument.canRead()) {
                val mediaList = ArrayList<MediaModel>()
                fileDocument.listFiles().forEach { file ->
                    if (file.name != ".nomedia" && file.isFile) {
                        val fileType = if (getFileExtension(file.name!!) == "mp4") "video" else "image"
                        mediaList.add(
                            MediaModel(
                                pathUri = file.uri.toString(),
                                fileName = file.name.toString(),
                                mediaType = fileType,
                            )
                        )
                    }
                }
                whatsappStatuses.postValue(mediaList)
                return mediaList
            } else {
                Log.e("Repository", "Failed to read directory.")
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching WhatsApp statuses: ${e.message}")
        }
        return ArrayList()
    }

    fun fetchWhatsappBusinessStatuses(activity: Activity): ArrayList<MediaModel> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val folderUriString = localUserManager.readwhatsappbusinessfolderuri().first()
                val folderUri = Uri.parse(folderUriString)

                withContext(Dispatchers.Main) {
                    try {
                        activity.contentResolver.takePersistableUriPermission(
                            folderUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                    } catch (e: SecurityException) {
                        Log.e("Repository", "Permission error: ${e.message}")
                        return@withContext
                    }
                }

                val fileDocument = DocumentFile.fromTreeUri(context, folderUri)
                if (fileDocument != null && fileDocument.canRead()) {
                    val mediaList = ArrayList<MediaModel>()
                    fileDocument.listFiles().forEach { file ->
                        if (file.name != ".nomedia" && file.isFile) {
                            val fileType = if (getFileExtension(file.name!!) == "mp4") "video" else "image"
                            mediaList.add(
                                MediaModel(
                                    pathUri = file.uri.toString(),
                                    fileName = file.name.toString(),
                                    mediaType = fileType,
                                )
                            )
                        }
                    }
                    withContext(Dispatchers.Main) {
                        whatsappBusinessStatuses.value = mediaList
                        for(i in mediaList){
                        }
                    }
                } else {
                    Log.e("Repository", "Failed to read directory.")
                }
            } catch (e: Exception) {
                Log.e("Repository", "Error fetching WhatsApp statuses: ${e.message}")
            }
        }
        return whatsappBusinessStatuses.value!!

    }

    private fun getFileExtension(fileName: String): String {
        return fileName.substringAfterLast(".")
    }
}
