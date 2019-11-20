package com.example.kuglll.shows_mark.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuglll.shows_mark.R
import com.example.kuglll.shows_mark.utils.Episode
import kotlinx.android.synthetic.main.episodes_item.view.*

class EpisodesAdapter(val dataset: List<Episode>, val itemOnClick : (String) -> Unit) :
    RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.episodes_item, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    inner class EpisodeViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(item: Episode){
            val formatedString = String.format("S%s E%s", item.seasonNumber, item.episodeNumber)
            itemView.episodeSeason.text = formatedString
            itemView.episodeTitle.text = item.title

            itemView.setOnClickListener {
                itemOnClick(item.id)
            }
        }
    }

}