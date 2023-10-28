package com.satria.dicoding.latihan.storyapp_submission.view.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satria.dicoding.latihan.storyapp_submission.databinding.StoryCardBinding
import com.satria.dicoding.latihan.storyapp_submission.model.api_response.ListStoryItem
import com.satria.dicoding.latihan.storyapp_submission.view.story_detail.StoryDetailActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val storyItem = getItem(position)
        holder.bind(storyItem ?: ListStoryItem())
        holder.itemView.setOnClickListener {

            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                holder.itemView.context as Activity,
                Pair(holder.storyCardBinding.imStoryImage, "im_story_image"),
                Pair(holder.storyCardBinding.tvUserName, "tv_user_name")

            )
            val intent = Intent(holder.itemView.context, StoryDetailActivity::class.java)
            intent.putExtra(StoryDetailActivity.EXTRA_DATA, storyItem)
            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    class MyViewHolder(val storyCardBinding: StoryCardBinding) :
        RecyclerView.ViewHolder(storyCardBinding.root) {
        fun bind(storyItem: ListStoryItem) {
            Glide.with(storyCardBinding.root.context).load(storyItem.photoUrl)
                .into(storyCardBinding.imStoryImage)
            storyCardBinding.tvUserName.text = storyItem.name
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