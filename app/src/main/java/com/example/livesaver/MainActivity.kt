package com.example.livesaver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.livesaver.home.HomeActivity
import com.example.livesaver.onboarding.OnBoardingActivity
import com.example.livesaver.onboarding.usescase.appentry.AppEntryUsecases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appEntryUsecases: AppEntryUsecases
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
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