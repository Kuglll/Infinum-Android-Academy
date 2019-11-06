package com.example.kuglll.shows_mark.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuglll.shows_mark.R
import com.example.kuglll.shows_mark.utils.Episode
import kotlinx.android.synthetic.main.episodes_item.view.*

class EpisodesAdapter(val dataset: List<Episode>) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.episodes_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formatedString = String.format("S%s E%s", dataset.get(position).seasonNumber, dataset.get(position).episodeNumber)
        holder.episodeSeason.text = formatedString
        holder.episodeTitle.text = "${dataset.get(position).title}"
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val episodeTitle = view.episodeTitle
    val episodeSeason = view.episodeSeason
}
