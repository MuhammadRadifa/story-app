package com.dicoding.storyapp.utils.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.databinding.CardViewItemBinding
import com.dicoding.storyapp.ui.detail.DetailStoryActivity
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import com.dicoding.storyapp.data.response.Story

class StoryAdapter :
    PagingDataAdapter<Story, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }
    }

    class MyViewHolder(private val binding: CardViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story){

            binding.textViewCardTitle.text = story.name
            binding.textViewCardDescription.text = story.description
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.imageViewCard)

            binding.root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        binding.root.context as Activity,
                        Pair(binding.imageViewCard as View, "profile"),
                        Pair(binding.textViewCardTitle as View, "name"),
                        Pair(binding.textViewCardDescription as View, "description")
                    )

                val intentDetailActivity = Intent(binding.root.context,DetailStoryActivity::class.java)
                intentDetailActivity.putExtra(DetailStoryActivity.EXTRA_ID,story.id)
                binding.root.context.startActivity(intentDetailActivity,optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}