package com.example.Kuglll.shows_mark

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.episodes_item.view.*
import kotlinx.android.synthetic.main.show_item.view.*

class EpisodesAdapter(val dataset: List<Episode>) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.episodes_item, parent, false))
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.episodeTitle.text = "${position+1}. ${dataset.get(position).title}"
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val episodeTitle = view.episodeTitle
}
