package com.example.livesaver.home.usecases.permissions

data class PermissionsUsecases(
    val readWhatappPermission: ReadWhatsAppPermission,
    val readWhatappBusinessPermission: ReadWhatsAppBusinessPermission,
    val whatsAppPermissionGranted: WhatsAppPermissionGranted,
    val whatsAppBusinessPermissionGranted: WhatsAppBusinessPermissionGranted
)
