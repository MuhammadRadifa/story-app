package com.dicoding.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.dicoding.storyapp.databinding.ActivityHomeBinding
import com.dicoding.storyapp.helper.ViewModelFactory
import com.dicoding.storyapp.ui.BaseClass
import com.dicoding.storyapp.ui.add.AddStoryActivity
import com.dicoding.storyapp.utils.adapter.LoadingStateAdapter
import com.dicoding.storyapp.utils.adapter.StoryAdapter

class HomeActivity : BaseClass() {

    private lateinit var binding: ActivityHomeBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val homeViewModel by viewModels<HomeViewModel> {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()



        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager


        binding.fab.setOnClickListener {
            val addIntent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(addIntent)
        }

        getData()

    }

    private fun getData() {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        homeViewModel.storyList.observe(this) {
            binding.shimmerLayout.apply {
                startShimmer()
                visibility = View.GONE
            }
            adapter.submitData(lifecycle, it)
        }
    }



}