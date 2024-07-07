package com.example.livesaver.home.presentation.activities

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livesaver.R

class ImagePreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        findViewById<ImageView>(R.id.imageView5).setImageURI(Uri.parse(intent.getStringExtra("pathUri")))
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            onBackPressed()
        }
    }
}