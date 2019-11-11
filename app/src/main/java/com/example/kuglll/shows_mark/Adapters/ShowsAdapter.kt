package com.example.kuglll.shows_mark.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuglll.shows_mark.R
import com.example.kuglll.shows_mark.utils.Show
import com.squareup.picasso.Picasso


import kotlinx.android.synthetic.main.show_item.view.*

class ShowsAdapter(private val dataset: List<Show>, val itemOnClick : (String, String) -> Unit) :
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
            Picasso.get().load("https://api.infinum.academy${item.imageUrl}")
                .placeholder(R.drawable.office) //using office as a placeholder
                .into(itemView.showImage)
            itemView.showName.text = item.title
            itemView.setOnClickListener {
                itemOnClick(item.id, item.title)
            }
        }


    }
}