package com.example.livesaver.home.presentation.activities

import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.livesaver.R

class VideoPreviewActivity : AppCompatActivity() {
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var videoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_preview)

        // Ensure the video URI is passed correctly
        val pathUri = intent.getStringExtra("pathUri")
        if (pathUri != null) {
            videoUri = Uri.parse(pathUri)
        } else {
            // Handle the error: URI is not passed correctly
            finish()
            return
        }

        // Initialize PlayerView
        playerView = findViewById(R.id.player)

        // Initialize ExoPlayer
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        // Prepare MediaItem
        val mediaItem = MediaItem.fromUri(videoUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
