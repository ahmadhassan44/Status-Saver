package com.example.livesaver.home.domain

data class MediaModel(
    val pathUri:String,
    val fileName:String,
    val mediaType:String,
    val isDownloaded:Boolean=false,
    val permanentlySaved:Boolean=false
)
