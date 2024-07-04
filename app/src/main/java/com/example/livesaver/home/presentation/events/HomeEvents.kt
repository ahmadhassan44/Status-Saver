package com.example.livesaver.home.presentation.events

sealed class HomeEvents {
     object GetWhatsappStoragePermission : HomeEvents()
     object GetBusinessStoragePermission : HomeEvents()
}