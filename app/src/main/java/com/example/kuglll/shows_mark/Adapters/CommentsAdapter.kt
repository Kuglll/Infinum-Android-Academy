package com.example.kuglll.shows_mark.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuglll.shows_mark.R
import com.example.kuglll.shows_mark.utils.Comment
import kotlinx.android.synthetic.main.comment_item.view.*


class CommentsAdapter(private val dataset: List<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(dataset[position])
    }


    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Comment) {
            itemView.userIcon.setImageResource(chooseRandomPicture())
            itemView.username.text = item.email.split("@")[0]
            itemView.userComment.text = item.text
        }

        fun chooseRandomPicture(): Int{
            val images: List<Int> = listOf(R.drawable.ic_img_placeholder_user_1,
                                        R.drawable.ic_img_placeholder_user_2,
                                        R.drawable.ic_img_placeholder_user_3)
            return images.shuffled().take(1)[0]
        }


    }


}