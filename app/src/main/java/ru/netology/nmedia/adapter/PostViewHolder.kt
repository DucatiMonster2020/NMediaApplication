package ru.netology.nmedia.adapter

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.post.countFormat

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            heart.isChecked = post.likedByMe
            heart.text = countFormat(post.countLikes)
            share.isChecked = post.sharedByMe
            share.text = countFormat(post.countShare)
            countView.text = countFormat(post.countView)

            heart.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_menu)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            videoContainer.visibility = if (post.videoUrl.isNullOrEmpty())
                View.GONE
            else View.VISIBLE
            if (!post.videoUrl.isNullOrEmpty()) {
                videoContainer.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}