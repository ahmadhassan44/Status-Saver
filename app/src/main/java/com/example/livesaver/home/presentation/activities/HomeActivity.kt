package com.example.livesaver.home.presentation.activities

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.livesaver.R
import com.example.livesaver.app.domain.AppMode
import com.example.livesaver.home.presentation.adapters.FragmentPagerAdapter
import com.example.livesaver.home.presentation.viewmodels.HomeViewModel
import com.example.livesaver.utilities.Constants
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(),PermissionRequester {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: FragmentPagerAdapter
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUpViews()
        intent.extras?.let {
            if(it.getBoolean("flag"))
                showPermissionBottomSheet()
        }
    }
    private fun setUpViews() {
        toolbar = findViewById(R.id.topAppBar)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        adapter = FragmentPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Images"
                1 -> tab.text = "Videos"
                2 -> tab.text = "Saved"
            }
        }.attach()
        viewPager.setCurrentItem(0, true)
        (toolbar.menu.findItem(R.id.action_filter)).setOnMenuItemClickListener {
            showModeBottomSheet()
            true
        }
        (toolbar.menu.findItem(R.id.action_whatsapp)).setOnMenuItemClickListener {
            Intent(Intent.ACTION_VIEW).also {
                it.component= ComponentName("com.whatsapp","com.whatsapp.Main")
                try {
                    startActivity(it)
                } catch (e:Exception){
                    Log.e("Error",e.message.toString())
                }
            }
            true
        }
        (toolbar.menu.findItem(R.id.info)).setOnMenuItemClickListener {
            startActivity(Intent(this,HowToUseActivity::class.java))
            true
        }
    }
    private fun showPermissionBottomSheet() {
        val sheet = layoutInflater.inflate(R.layout.permissions_bottomsheet, null)
        val allowBtn=sheet.findViewById<Button>(R.id.allow)
        val storageCheckBox=sheet.findViewById<CheckBox>(R.id.storage)
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
        viewModel.isChecked1.observe(this) { isChecked ->
            storageCheckBox.isChecked = isChecked
        }
        viewModel.isVisable.observe(this, Observer { isVisible ->
            allowBtn.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        })
        allowBtn.setOnClickListener {
            val storageChecked = storageCheckBox.isChecked
            when {
                storageChecked -> {
                    getPermission()
                }
            }
            dialog.dismiss()
        }
    }
    @SuppressLint("InlinedApi")
     fun getPermission() {
        if(viewModel.appModeState.value==AppMode.WHATSAPP) {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, Constants.getWhatsappUri())
                putExtra("android.content.extra.SHOW_ADVANCED", true)
            }
            startActivityForResult(intent, Constants.WHATSAPP_REQUEST_CODE)
        } else {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, Constants.getWhatsappBusinessUri())
                putExtra("android.content.extra.SHOW_ADVANCED", true)
            }
            startActivityForResult(intent, Constants.WHATSAPP_BUSINESS_REQUEST_CODE)
        }
    }
    private fun showModeBottomSheet(){
        val sheet=layoutInflater.inflate(R.layout.app_mode_bottomsheet,null)
        val dialog=BottomSheetDialog(this)
        dialog.setContentView(sheet)
        dialog.show()
        val wamode=dialog.findViewById<ConstraintLayout>(R.id.wamode)
        val wabmode=dialog.findViewById<ConstraintLayout>(R.id.wabmode)
        val watick=dialog.findViewById<ImageView>(R.id.tick1)
        val wabtick=dialog.findViewById<ImageView>(R.id.tick2)
        viewModel.appModeState.observe(this) {
            when (it) {
                AppMode.WHATSAPP -> {
                    wamode?.setBackgroundColor(Color.parseColor("#C7F2E8"))
                    watick?.visibility=View.VISIBLE

                    wabmode?.setBackgroundColor(Color.TRANSPARENT)
                    wabtick?.visibility=View.INVISIBLE
                }
                AppMode.WHATSAPP_BUSINESS -> {
                    wabtick?.visibility=View.VISIBLE
                    wabmode?.setBackgroundColor(Color.parseColor("#C7F2E8"))

                    wamode?.setBackgroundColor(Color.TRANSPARENT)
                    watick?.visibility=View.INVISIBLE
                }
            }
        }
        wamode?.setOnClickListener {
            lifecycleScope.launch {
                viewModel.changeAppMode(AppMode.WHATSAPP)
                delay(100)
                dialog.dismiss()
            }
        }
        wabmode?.setOnClickListener {
            lifecycleScope.launch {
                viewModel.changeAppMode(AppMode.WHATSAPP_BUSINESS)
                delay(100)
                dialog.dismiss()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK){
            val uri=data?.data
            if(requestCode==Constants.WHATSAPP_REQUEST_CODE){
                viewModel.whatsappPermissionGranted(uri!!)

            } else if(requestCode==Constants.WHATSAPP_BUSINESS_REQUEST_CODE) {
                viewModel.whatsappBusinessPermissionGranted(uri!!)
            }
            try {
                this.contentResolver.takePersistableUriPermission(
                    uri!!, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                Log.e("Permissions", "Permission error: ${e.message}")
            }
        }
    }

    override fun requestStoragePermission() {
        getPermission()
    }

    override fun onBackPressed() {
        val sheet=layoutInflater.inflate(R.layout.exit_bottom_sheet,null)
        val dialog=BottomSheetDialog(this)
        dialog.setContentView(sheet)
        dialog.show()
        sheet.findViewById<Button>(R.id.exit).setOnClickListener{
            finishAffinity()
        }
        sheet.findViewById<Button>(R.id.notnow).setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshRepository()
    }
}