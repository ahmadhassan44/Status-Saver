package com.example.livesaver.utilities

import android.net.Uri
import android.os.Build

object Constants {
    const val WHATSAPP_REQUEST_CODE = 1001
    const val WHATSAPP_BUSINESS_REQUEST_CODE = 1002
    val WHATSAPP_PATH_URI_ANDROID =
        Uri.parse("content://com.android.externalstorage.documents/document/primary%3AWhatsApp%2FMedia%2F.Statuses")
    val WHATSAPP_PATH_URI_ANDROID_11 =
        Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses")

    val WHATSAPP_BUSINESS_PATH_URI_ANDROID =
        Uri.parse("content://com.android.externalstorage.documents/document/primary%3AWhatsApp%20Business%2FMedia%2F.Statuses")
    val WHATSAPP_BUSINESS_PATH_URI_ANDROID_11 =
        Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp%20Business%2FMedia%2F.Statuses")

    fun getWhatsappUri(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WHATSAPP_PATH_URI_ANDROID_11
        } else {
            WHATSAPP_PATH_URI_ANDROID_11
        }
    }

    fun getWhatsappBusinessUri(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WHATSAPP_BUSINESS_PATH_URI_ANDROID_11
        } else {
            WHATSAPP_BUSINESS_PATH_URI_ANDROID
        }
    }

}