package com.example.livesaver.home.presentation.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.livesaver.R
import com.example.livesaver.app.domain.AppMode
import com.example.livesaver.home.presentation.adapters.FragmentPagerAdapter
import com.example.livesaver.home.presentation.viewmodels.HomeViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: FragmentPagerAdapter
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolbar=findViewById(R.id.topAppBar)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        adapter = FragmentPagerAdapter(supportFragmentManager, lifecycle)
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
        viewPager.setCurrentItem(1, false)
        (toolbar.menu.findItem(R.id.action_whatsapp)).setOnMenuItemClickListener {
            showModeBottomSheet()
            true
        }
        showPermissionBottomSheet()

    }

    private fun showPermissionBottomSheet() {
        val sheet = layoutInflater.inflate(R.layout.permissions_bottomsheet, null)
        val allowBtn=sheet.findViewById<Button>(R.id.allow)
        val storageCheckBox=sheet.findViewById<CheckBox>(R.id.storage)
        val notificationCheckBox=sheet.findViewById<CheckBox>(R.id.notifications)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(sheet)
        dialog.show()
        dialog.setCancelable(true)
        sheet.findViewById<ImageButton>(R.id.close).setOnClickListener {
            dialog.dismiss()
        }
        storageCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onCheckbox1Toggled(isChecked)
        }
        notificationCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onCheckbox2Toggled(isChecked)
        }
        viewModel.isChecked1.observe(this) { isChecked ->
            storageCheckBox.isChecked = isChecked
        }

        viewModel.isChecked2.observe(this, Observer { isChecked ->
            notificationCheckBox.isChecked = isChecked
        })

        viewModel.isVisable.observe(this, Observer { isVisible ->
            allowBtn.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        })
        allowBtn.setOnClickListener {
            val storageChecked = storageCheckBox.isChecked
            val notificationChecked = notificationCheckBox.isChecked
            when {
                storageChecked && notificationChecked -> {

                }
                storageChecked -> {
                    // Request storage permission
                }
                notificationChecked -> {
                    // Request notification permission
                }
            }
            dialog.dismiss()
        }
    }
    private fun showModeBottomSheet(){
        val sheet=layoutInflater.inflate(R.layout.app_mode_bottomsheet,null)
        val dialog=BottomSheetDialog(this)
        dialog.setContentView(sheet)
        dialog.show()
        val wacheck=sheet.findViewById<RadioButton>(R.id.wa)
        val wabcheck=sheet.findViewById<RadioButton>(R.id.wab)
        viewModel.appModeState.observe(this) {
            when (it) {
                AppMode.WHATSAPP -> {
                    wacheck.isChecked = true
                }
                AppMode.WHATSAPP_BUSINESS -> {
                    wabcheck.isChecked = true
                }
            }
        }
        wacheck.setOnClickListener {
            viewModel.changeAppMode(AppMode.WHATSAPP)
            lifecycleScope.launch {
                delay(400)
                dialog.dismiss()
            }
        }
        wabcheck.setOnClickListener {
            viewModel.changeAppMode(AppMode.WHATSAPP_BUSINESS)
            lifecycleScope.launch {
                delay(400)
                dialog.dismiss()
            }
        }

    }
}