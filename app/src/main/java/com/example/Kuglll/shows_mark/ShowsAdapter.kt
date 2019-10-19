package com.example.Kuglll.shows_mark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.show_item.view.*

class ShowsAdapter(private val dataset: List<Show>) :
    RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.show_item, parent, false)
        return ShowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(dataset[position])
    }


    inner class ShowViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Show) {
            itemView.showImage.setImageResource(item.imageID)
            itemView.showName.text = item.name
            if(item.end_date != 0){
                itemView.showDate.text = "(${item.start_date} - ${item.end_date})"
            } else {
                itemView.showDate.text = "(${item.start_date} - )"
            }
            itemView.setOnClickListener {
                //on click
            }
        }
    }
}