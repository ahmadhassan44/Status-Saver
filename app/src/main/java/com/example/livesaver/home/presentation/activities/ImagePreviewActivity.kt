package com.example.livesaver.home.presentation.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.livesaver.R
import com.example.livesaver.home.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePreviewActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        val downLoadBtn=findViewById<ImageButton>(R.id.saveImage)
        val savedIcon=findViewById<ImageButton>(R.id.savedIcon2)
        val downldoadText=findViewById<TextView>(R.id.imageDownload)
        if(intent.getBooleanExtra("isDownloaded",false)){
            homeViewModel.saveMedia(intent.getStringExtra("pathUri")!!,intent.getStringExtra("fileName")!!)
            downLoadBtn.visibility=View.GONE
            downLoadBtn.isEnabled=false
            savedIcon.visibility=View.VISIBLE
            downldoadText.text="Saved"
        }
        findViewById<ImageView>(R.id.imageView5).setImageURI(Uri.parse(intent.getStringExtra("pathUri")))
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            onBackPressed()
        }
        downLoadBtn.setOnClickListener {
            homeViewModel.saveMedia(intent.getStringExtra("pathUri")!!,intent.getStringExtra("fileName")!!)
            downLoadBtn.visibility=View.GONE
            downLoadBtn.isEnabled=false
            savedIcon.visibility=View.VISIBLE
            downldoadText.text="Saved"
        }
    }
}