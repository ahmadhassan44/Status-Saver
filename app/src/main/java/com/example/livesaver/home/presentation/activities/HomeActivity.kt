package com.example.livesaver.home.presentation.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.livesaver.R
import com.example.livesaver.home.presentation.adapters.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: FragmentPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        intent.extras?.let {
            if(it.getBoolean("flag")) {
                Toast.makeText(this, "App opened for the first time", Toast.LENGTH_LONG).show()
            }
        }
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        adapter = FragmentPagerAdapter(supportFragmentManager,lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_settings, null)
                }
                1 -> tab.text = "Images"
                2 -> tab.text = "Videos"
                3 -> tab.text = "Saved"
            }
        }.attach()
    }
}