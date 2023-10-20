package com.satria.dicoding.latihan.storyapp_submission.view.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satria.dicoding.latihan.storyapp_submission.databinding.StoryCardBinding
import com.satria.dicoding.latihan.storyapp_submission.model.api_response.ListStoryItem
import com.satria.dicoding.latihan.storyapp_submission.view.story_detail.StoryDetailActivity

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val storyItem = getItem(position)
        holder.bind(storyItem)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, StoryDetailActivity::class.java)
            intent.putExtra(StoryDetailActivity.EXTRA_DATA, storyItem)
            holder.itemView.context.startActivity(intent)
        }
    }

    class MyViewHolder(private val binding: StoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem) {
            Glide.with(binding.root.context).load(storyItem.photoUrl).into(binding.imStoryImage)
            binding.tvUserName.text = storyItem.name
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }


        }
    }
}