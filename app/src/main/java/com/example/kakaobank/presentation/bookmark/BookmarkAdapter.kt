package com.example.kakaobank.presentation.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kakaobank.R
import com.example.kakaobank.databinding.ItemMediaBinding
import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.model.MediaType

class BookmarkAdapter(
    private val onBookmarkClick: (MediaItem) -> Unit,
) : ListAdapter<MediaItem, BookmarkAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MediaItem) {
            Glide.with(binding.root)
                .load(item.imageUrl)
                .centerCrop()
                .into(binding.ivThumbnail)

            binding.tvDatetime.text = item.datetime.take(10)
            binding.tvType.text = if (item.type == MediaType.IMAGE) "IMAGE" else "VIDEO"
            binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_filled)
            binding.ivBookmark.setOnClickListener { onBookmarkClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MediaItem>() {
        override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
            oldItem.imageUrl == newItem.imageUrl

        override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
            oldItem == newItem
    }
}
