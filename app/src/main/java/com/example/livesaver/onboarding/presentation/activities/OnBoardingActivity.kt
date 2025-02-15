package com.example.livesaver.onboarding.presentation.activities

import android.content.Intent
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.livesaver.utilities.AppUtils
import com.example.livesaver.R
import com.example.livesaver.home.presentation.activities.HomeActivity
import com.example.livesaver.onboarding.presentation.ui_events.OnBoardingEvent
import com.example.livesaver.onboarding.presentation.viewmodels.OnBoardingViewModel
import com.example.livesaver.onboarding.presentation.adapters.LanguageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private val viewModel: OnBoardingViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        observeState()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun observeState() {
        viewModel.currentScreen.observe(this) {
            when(it) {
                1-> {
                    setContentView(R.layout.app_intro_screen)
                    val stream=resources.openRawResource(R.raw.appinfo)
                    val drawable= AnimatedImageDrawable.createFromStream(stream,null) as AnimatedImageDrawable
                    findViewById<ImageView>(R.id.imageView).setImageDrawable(drawable)
                    drawable.start()
                    findViewById<Button>(R.id.getstartedbtn).setOnClickListener{
                        viewModel.onEvent(OnBoardingEvent.GetStartedClicked)
                    }
                }
                2-> {
                    setContentView(R.layout.progress_screen)
                    val progressBar = findViewById<ProgressBar>(R.id.progressBar)
                    val progressText = findViewById<TextView>(R.id.textView4)
                    viewModel.progress.value?.let {
                        viewModel.startProgressBar()
                        viewModel.progress.observe(this){
                            progressBar.setProgress(it)
                            progressText.setText("$it%")
                        }
                    }
                }
                3-> {
                    setContentView(R.layout.select_language_screen)
                    val recyclerView=findViewById<RecyclerView>(R.id.recview)
                    val adapter= LanguageAdapter(AppUtils().getLanguages())
                    recyclerView.layoutManager=LinearLayoutManager(this)
                    recyclerView.adapter=adapter
                    findViewById<Button>(R.id.button3).setOnClickListener{
                        viewModel.onEvent(OnBoardingEvent.SaveAppEntry)
                        val intent=Intent(this, HomeActivity::class.java)
                        intent.putExtra("flag",true)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}