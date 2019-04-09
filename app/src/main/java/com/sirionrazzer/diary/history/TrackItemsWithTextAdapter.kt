package com.sirionrazzer.diary.history

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import android.view.ViewGroup
import android.widget.LinearLayout
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem

class TrackItemsWithTextAdapter(
    private val context: Context,
    private val trackItems: ArrayList<TrackItem>,
    private val clickListener: (String) -> Unit)
    : RecyclerView.Adapter<TrackItemsWithTextAdapter.ViewHolder>() {

    class ViewHolder(val templateItemLayout: LinearLayout) : RecyclerView.ViewHolder(templateItemLayout) {
        fun bind(trackItemName: String, clickListener: (String) -> Unit) {
            templateItemLayout.setOnClickListener { clickListener(trackItemName) }
        }
    }

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TrackItemsWithTextAdapter.ViewHolder {

        val templateItemLayout = inflater.inflate(R.layout.template_item_with_text, parent, false) as LinearLayout

        return TrackItemsWithTextAdapter.ViewHolder(templateItemLayout)
    }

    override fun onBindViewHolder(holder: TrackItemsWithTextAdapter.ViewHolder, position: Int) {
        val templateItemLayout = holder.templateItemLayout
        val nameTextView = templateItemLayout.findViewById<TextView>(R.id.trackItemName)
        nameTextView.text = trackItems[position].name

        holder.bind(trackItems[position].name, clickListener)
    }

    override fun getItemCount(): Int {
        return trackItems.size
    }

}
