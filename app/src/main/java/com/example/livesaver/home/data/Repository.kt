package com.example.livesaver.home.data

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import com.example.livesaver.app.domain.LocalUserManager
import com.example.livesaver.home.domain.MediaModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class Repository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localUserManager: LocalUserManager,
) {
    val allStatuses = MutableLiveData(ArrayList<MediaModel>())
    val savedStatuses = MutableLiveData(HashMap<String, MediaModel>())
    fun fetchSavedStatuses(): Map<String, MediaModel> {
        val directoryName = "Status Saver"
        val savedFilesMap = mutableMapOf<String, MediaModel>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 and above (scoped storage)
            val projection = arrayOf(
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.RELATIVE_PATH,
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.MIME_TYPE
            )
            val selection = "${MediaStore.MediaColumns.RELATIVE_PATH} LIKE ?"
            val selectionArgs = arrayOf("%$directoryName%")
            val sortOrder = "${MediaStore.MediaColumns.DATE_ADDED} DESC"

            val queryUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val cursor: Cursor? = context.contentResolver.query(
                queryUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            cursor?.use {
                val columnDisplayName = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                val columnRelativePath = it.getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH)
                val columnId = it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val columnMimeType = it.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)

                while (it.moveToNext()) {
                    val fileName = it.getString(columnDisplayName)
                    val id = it.getLong(columnId)
                    val mimeType = it.getString(columnMimeType)
                    val contentUri = ContentUris.withAppendedId(queryUri, id)

                    val mediaType = if (mimeType.startsWith("video")) "video" else "image"
                    savedFilesMap[fileName] = MediaModel(
                        pathUri = contentUri.toString(),
                        fileName = fileName,
                        mediaType = mediaType,
                        isDownloaded = true
                    )
                    Log.d("Saved", savedFilesMap[fileName].toString())
                }
            }
        } else {
            // Android 9 and below (direct file access)
            val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val directory = File(downloadsDirectory, directoryName)
            if (directory.exists() && directory.isDirectory) {
                directory.listFiles()?.forEach { file ->
                    val mediaType = if (file.extension == "mp4") "video" else "image"
                    savedFilesMap[file.name] = MediaModel(
                        pathUri = file.absolutePath,
                        fileName = file.name,
                        mediaType = mediaType,
                        isDownloaded = true
                    )
                    Log.d("Saved", savedFilesMap[file.name].toString())
                }
            }
        }

        savedStatuses.postValue(savedFilesMap as HashMap<String, MediaModel>?)
        return savedFilesMap
    }

    suspend fun fetchWhatsappStatuses(): ArrayList<MediaModel> {
        try {
            val folderUriString = localUserManager.readwhatsappfolderuri().first()
            val folderUri = Uri.parse(folderUriString)

            val fileDocument = withContext(Dispatchers.IO) {
                DocumentFile.fromTreeUri(context, folderUri)
            }
            if (fileDocument != null && fileDocument.canRead()) {
                val mediaList = ArrayList<MediaModel>()
                fileDocument.listFiles().forEach { file ->
                    if (file.name != ".nomedia" && file.isFile) {
                        val fileType = if (getFileExtension(file.name!!) == "mp4") "video" else "image"
                        if(savedStatuses.value!!.containsKey(file.name.toString())) {
                            mediaList.add(
                                MediaModel(
                                    pathUri = file.uri.toString(),
                                    fileName = file.name.toString(),
                                    mediaType = fileType,
                                    isDownloaded = true
                                )
                            )
                        } else {
                            mediaList.add(
                                MediaModel(
                                    pathUri = file.uri.toString(),
                                    fileName = file.name.toString(),
                                    mediaType = fileType,
                                    isDownloaded = false
                                )
                            )
                        }
                        Log.d("Added", file.uri.toString())
                    }
                }
                allStatuses.postValue(mediaList)
                return mediaList
            } else {
                Log.e("Repository", "Failed to read directory.")
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching WhatsApp statuses: ${e.message}")
        }
        return ArrayList()
    }

    suspend fun fetchWhatsappBusinessStatuses(): ArrayList<MediaModel> {
        try {
            val folderUriString = localUserManager.readwhatsappbusinessfolderuri().first()
            val folderUri = Uri.parse(folderUriString)

            val fileDocument = withContext(Dispatchers.IO) {
                DocumentFile.fromTreeUri(context, folderUri)
            }
            if (fileDocument != null && fileDocument.canRead()) {
                val mediaList = ArrayList<MediaModel>()
                fileDocument.listFiles().forEach { file ->
                    if (file.name != ".nomedia" && file.isFile) {
                        val fileType = if (getFileExtension(file.name!!) == "mp4") "video" else "image"
                        if(savedStatuses.value!!.containsKey(file.name.toString())) {
                            mediaList.add(
                                MediaModel(
                                    pathUri = file.uri.toString(),
                                    fileName = file.name.toString(),
                                    mediaType = fileType,
                                    isDownloaded = true
                                )
                            )
                        } else {
                            mediaList.add(
                                MediaModel(
                                    pathUri = file.uri.toString(),
                                    fileName = file.name.toString(),
                                    mediaType = fileType,
                                    isDownloaded = false
                                )
                            )
                        }
                        Log.d("Added", file.uri.toString())
                    }
                }
                allStatuses.postValue(mediaList)
                return mediaList
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
    suspend fun saveStatus(uriString: String, fileName: String) {
        withContext(Dispatchers.IO) {
            val directoryName = "Status Saver"
            val inputUri = Uri.parse(uriString)

            try {
                val outputStream: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10 and above (scoped storage)
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        val mimeType = when (getFileExtension(fileName)) {
                            "mp4" -> "video/mp4"
                            "jpg", "jpeg" -> "image/jpeg"
                            "png" -> "image/png"
                            else -> "application/octet-stream"
                        }
                        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                        put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/$directoryName")
                    }
                    val resolver = context.contentResolver
                    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    if (uri == null) {
                        Log.e("SaveMedia", "Error: Could not insert MediaStore entry.")
                    }
                    uri?.let { resolver.openOutputStream(it) }
                } else {
                    // Android 9 and below (direct file access)
                    val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val directory = File(downloadsDirectory, directoryName)
                    if (!directory.exists()) {
                        directory.mkdirs() // Create directory if it doesn't exist
                    }
                    val file = File(directory, fileName)
                    FileOutputStream(file)
                }

                if (outputStream != null) {
                    val inputStream = context.contentResolver.openInputStream(inputUri)
                    if (inputStream != null) {
                        inputStream.copyTo(outputStream)
                        inputStream.close()
                        outputStream.close()
                        Log.d("SaveMedia", "Media saved successfully")
                    } else {
                        Log.e("SaveMedia", "Error: Input stream is null")
                    }
                } else {
                    Log.e("SaveMedia", "Error: Output stream is null")
                }
            } catch (e: Exception) {
                Log.e("SaveMedia", "Error saving media: ${e.message}")
            }
        }
    }
    fun getDirectoryUri(directoryName: String): Uri {
        // Get the Downloads directory path
        val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Create a File object for the subdirectory
        val subdirectory = File(downloadsPath, directoryName)

        // Convert the File object to a URI using Uri.fromFile
        return Uri.fromFile(subdirectory)
    }
}
