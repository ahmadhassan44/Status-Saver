package com.example.livesaver.home.presentation.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.livesaver.R
import com.example.livesaver.home.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoPreviewActivity : AppCompatActivity() {
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var videoUri: Uri
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_preview)
        val downLoadBtn=findViewById<ImageButton>(R.id.saveVideo)
        val savedIcon=findViewById<ImageButton>(R.id.savedIcon)
        val downldoadText=findViewById<TextView>(R.id.textView18)
        if(intent.getBooleanExtra("isDownloaded",false)){
            homeViewModel.saveMedia(intent.getStringExtra("pathUri")!!,intent.getStringExtra("fileName")!!)
            downLoadBtn.visibility=View.GONE
            downLoadBtn.isEnabled=false
            savedIcon.visibility=View.VISIBLE
            downldoadText.text="Saved"
        }
        val pathUri = intent.getStringExtra("pathUri")
        if (pathUri != null) {
            videoUri = Uri.parse(pathUri)
        } else {
            finish()
            return
        }
        playerView = findViewById(R.id.player)
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        val mediaItem = MediaItem.fromUri(videoUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            onBackPressed()
        }
        downLoadBtn.setOnClickListener{
            homeViewModel.saveMedia(intent.getStringExtra("pathUri")!!,intent.getStringExtra("fileName")!!)
            downLoadBtn.visibility=View.GONE
            downLoadBtn.isEnabled=false
            savedIcon.visibility=View.VISIBLE
            downldoadText.text="Saved"
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
