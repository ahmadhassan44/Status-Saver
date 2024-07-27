package com.example.livesaver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.livesaver.home.presentation.activities.HomeActivity
import com.example.livesaver.onboarding.presentation.activities.OnBoardingActivity
import com.example.livesaver.onboarding.usescases.appentry.AppEntryUsecases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appEntryUsecases: AppEntryUsecases
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch {
            // Wait for a single emission and then set the state
            val hasSeenOnboarding = appEntryUsecases.readAppEntry().first()
            // Start the appropriate activity
            val intent = if (!hasSeenOnboarding) {
                Intent(this@MainActivity, OnBoardingActivity::class.java)
            } else {
                Intent(this@MainActivity, HomeActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}