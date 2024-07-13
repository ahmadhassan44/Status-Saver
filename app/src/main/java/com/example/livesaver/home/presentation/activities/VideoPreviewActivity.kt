package com.example.livesaver.home.presentation.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.livesaver.R
import com.example.livesaver.home.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoPreviewActivity : AppCompatActivity() {
    private lateinit var videoUri: Uri
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var  videoView:VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_preview)
        val downLoadBtn=findViewById<ImageButton>(R.id.saveVideo)
        val sharebtn=findViewById<ImageButton>(R.id.sharebtn)
        val repostbtn=findViewById<ImageButton>(R.id.repostbtn)
        val savedIcon=findViewById<ImageButton>(R.id.savedvideoicon)
        val downldoadText=findViewById<TextView>(R.id.textView18)
        val deleteVideo=findViewById<ImageButton>(R.id.deletevideo)
        if(intent.getBooleanExtra("isDownloaded",false)){
            downLoadBtn.visibility=View.GONE
            downLoadBtn.isEnabled=false
            downLoadBtn.isClickable=false
            savedIcon.visibility=View.VISIBLE
            downldoadText.text="Saved"
        }
        if(intent.getBooleanExtra("permanentlySaved",false)){
            downLoadBtn.visibility=View.GONE
            downLoadBtn.isEnabled=false
            downLoadBtn.isClickable=false
            savedIcon.visibility=View.GONE
            downldoadText.text="Delete"
            deleteVideo.visibility=View.VISIBLE
        }
        val pathUri = intent.getStringExtra("pathUri")
        if (pathUri != null) {
            videoUri = Uri.parse(pathUri)
        } else {
            finish()
            return
        }
        videoView=findViewById<VideoView>(R.id.videoview)
        videoView.setVideoURI(videoUri)
        val controller=MediaController(this)
        videoView.setMediaController(controller)
        val playButton = findViewById<ImageButton>(R.id.play_button)
        playButton.setOnClickListener {
            if (!videoView.isPlaying) {
                videoView.start()
                playButton.visibility = View.GONE
            }
        }
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            onBackPressed()
        }
        downLoadBtn.setOnClickListener{
            homeViewModel.saveMedia(intent.getStringExtra("pathUri")!!,intent.getStringExtra("fileName")!!)
            downLoadBtn.visibility=View.GONE
            downLoadBtn.isEnabled=false
            downLoadBtn.isClickable=false
            savedIcon.visibility=View.VISIBLE
            downldoadText.text="Saved"
        }
        sharebtn.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "video/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri)
            startActivity(Intent.createChooser(shareIntent, "Share Video"))
        }
        repostbtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "video/*"
            shareIntent.setPackage("com.whatsapp")
            shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri)
            try {
                startActivity(Intent.createChooser(shareIntent, "Share Video"))
            } catch (e: ActivityNotFoundException) {
                // WhatsApp not installed or no activity to handle the intent
                Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
            }
        }
        savedIcon.setOnClickListener {
            Toast.makeText(this, "Already Saved!", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onPause() {
        super.onPause()
        if (::videoView.isInitialized) {
            videoView.pause()
        }
    }
}
