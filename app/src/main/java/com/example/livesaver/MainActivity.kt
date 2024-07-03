package com.example.livesaver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.livesaver.home.presentation.activities.HomeActivity
import com.example.livesaver.onboarding.presentation.activities.OnBoardingActivity
import com.example.livesaver.onboarding.usescases.appentry.AppEntryUsecases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appEntryUsecases: AppEntryUsecases
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            appEntryUsecases.readAppEntry().collect{
                Log.d("aht", "onCreate: $it")
                if(!it) {
                    startActivity(Intent(applicationContext, OnBoardingActivity::class.java))
                    finish()
                }
                else{
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                }
            }
        }
    }
}