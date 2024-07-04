package com.example.livesaver.home.permissionsUsecases

data class PermissionsUsecases(
    val readWhatappPermission: ReadWhatsAppPermission,
    val readWhatappBusinessPermission: ReadWhatsAppBusinessPermission,
    val whatsAppPermissionGranted: WhatsAppPermissionGranted,
    val whatsAppBusinessPermissionGranted: WhatsAppBusinessPermissionGranted
)
