package com.example.livesaver.home.presentation.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        val savedIcon=findViewById<ImageButton>(R.id.savedimageicon)
        val sharebtn=findViewById<ImageButton>(R.id.imageshare)
        val repostbtn=findViewById<ImageButton>(R.id.imagerepost)
        val downldoadText=findViewById<TextView>(R.id.imageDownload)
        if(intent.getBooleanExtra("isDownloaded",false)){
            downLoadBtn.visibility=View.GONE
            downLoadBtn.isEnabled=false
            downLoadBtn.isClickable=false
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
            downLoadBtn.isClickable=false
            savedIcon.visibility=View.VISIBLE
            downldoadText.text="Saved"
            Log.d("savingfix","clicked")
        }
        sharebtn.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(intent.getStringExtra("pathUri")))
            startActivity(Intent.createChooser(shareIntent, "Share Image"))
        }
        repostbtn.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.setPackage("com.whatsapp")
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(intent.getStringExtra("pathUri")))
            try {
                startActivity(Intent.createChooser(shareIntent, "Share Image"))
            } catch (e: ActivityNotFoundException) {
                // WhatsApp not installed or no activity to handle the intent
                Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
            }
        }
        savedIcon.setOnClickListener {
            Toast.makeText(this,"Already Saved!",Toast.LENGTH_SHORT).show()
        }
    }
}