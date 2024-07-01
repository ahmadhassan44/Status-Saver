package com.example.livesaver.onboarding

import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livesaver.R

class OnBoardingActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.app_intro_screen)
        val stream=resources.openRawResource(R.raw.appinfo)
        val drawable=AnimatedImageDrawable.createFromStream(stream,null) as AnimatedImageDrawable
        findViewById<ImageView>(R.id.imageView).setImageDrawable(drawable)
        drawable.start()
        findViewById<Button>(R.id.getstartedbtn).setOnClickListener {
            setContentView(R.layout.activity_main)
        }
        println()
    }
}