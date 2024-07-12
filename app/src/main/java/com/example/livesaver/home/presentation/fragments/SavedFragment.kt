package com.example.livesaver.home.presentation.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.livesaver.R
import com.example.livesaver.home.domain.MediaModel
import com.example.livesaver.home.presentation.activities.HowToUseActivity
import com.example.livesaver.home.presentation.activities.PermissionRequester
import com.example.livesaver.home.presentation.adapters.MediaAdapter
import com.example.livesaver.home.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class SavedFragment : Fragment() {
    private lateinit var savedScreen:View
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var permissionRequester: PermissionRequester? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PermissionRequester) {
            permissionRequester = context
        } else {
            throw RuntimeException("$context must implement PermissionRequester")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        savedScreen= inflater.inflate(R.layout.fragment_saved, container, false)
        return savedScreen
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val swipeRefreshLayout=savedScreen.findViewById<SwipeRefreshLayout>(R.id.refresedSaved)
        val savedRecView=savedScreen.findViewById<RecyclerView>(R.id.savedrecview)
        savedRecView.layoutManager=GridLayoutManager(requireContext(),3)
        lifecycleScope.launch {
            homeViewModel.noPermissionState.observe(viewLifecycleOwner) {
                if(it){
                    savedScreen.findViewById<View>(R.id.nopermissionsview).visibility=View.VISIBLE
                    swipeRefreshLayout.visibility=View.GONE
                }
                else{
                    savedScreen.findViewById<View>(R.id.nosavedmedia).findViewById<Button>(R.id.howtousebtn).setOnClickListener {
                        startActivity(Intent(requireContext(), HowToUseActivity::class.java))
                    }
                    savedScreen.findViewById<View>(R.id.nopermissionsview).visibility=View.GONE
                    swipeRefreshLayout.visibility=View.VISIBLE
                    val adapter= MediaAdapter(ArrayList<MediaModel>())
                    savedRecView.adapter=adapter
                    swipeRefreshLayout.setOnRefreshListener {
                        swipeRefreshLayout.isRefreshing=false
                        homeViewModel.refreshRepository()
                    }
                    homeViewModel.savedStatuses.observe(viewLifecycleOwner){
                        it?.let {
                            val list=it.values.toList()
                            adapter.updateList(list)
                            Log.d("SavedFragment", "Adapter updated with list of size: ${it.size}")
                            if(list.isEmpty()){
                                savedScreen.findViewById<View>(R.id.nosavedmedia).visibility=View.VISIBLE
                                savedRecView.visibility=View.GONE
                            }
                            else{
                                savedScreen.findViewById<View>(R.id.nosavedmedia).visibility=View.GONE
                                savedRecView.visibility=View.VISIBLE
                            }
                        }
                    }
                    savedRecView.adapter=adapter
                }
            }
        }
        savedScreen.findViewById<View>(R.id.nopermissionsview).findViewById<Button>(R.id.button).setOnClickListener {
            permissionRequester?.requestStoragePermission()
        }
    }
    override fun onDetach() {
        super.onDetach()
        permissionRequester = null
    }
}