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

    val whatsappStatuses = MutableLiveData<ArrayList<MediaModel>>(ArrayList<MediaModel>())

    suspend fun fetchWhatsappStatuses(): ArrayList<MediaModel>{
        try {
            val folderUriString = localUserManager.readwhatsappfolderuri().first()
            val folderUri = Uri.parse(folderUriString)

            withContext(Dispatchers.Main) {
                try {
                    // Request permission to access the folder URI
                    context.contentResolver.takePersistableUriPermission(
                        folderUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    Log.e("Repository", "Permission error: ${e.message}")
                }
            }

            // Fetch the document file from the folder URI
            val fileDocument = withContext(Dispatchers.IO) {
                DocumentFile.fromTreeUri(context, folderUri)
            }

            // Process the fetched documents if they are readable
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
                        Log.d("Repository", "Added ${file.name}")
                    }
                }
                // Update the LiveData with the fetched media list
                whatsappStatuses.postValue(mediaList)
                return mediaList
                Log.d("Repository", "WhatsApp statuses fetched successfully with length ${mediaList.size}")
            } else {
                Log.e("Repository", "Failed to read directory.")
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching WhatsApp statuses: ${e.message}")
        }
        return ArrayList()
    }

    private fun getFileExtension(fileName: String): String {
        return fileName.substringAfterLast(".")
    }
}
