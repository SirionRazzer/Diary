package com.sirionrazzer.diary.stats

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.util.DateUtils
import com.squareup.picasso.Picasso

class TrackItemStatsAdapter(private val stats: List<TrackItem>) :
    RecyclerView.Adapter<TrackItemStatsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.stat_item, parent, false))
    }

    override fun getItemCount(): Int {
        return stats.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stats[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var date = itemView.findViewById<TextView>(R.id.tvStatItemDate)
        private var ivImage = itemView.findViewById<ImageView>(R.id.ivTrackItemImage)
        private var tvNumber = itemView.findViewById<TextView>(R.id.tvStatItemNumber)
        private var tvText = itemView.findViewById<TextView>(R.id.tvStatItemText)
        private val dateUtils = DateUtils()

        fun bind(item: TrackItem) {

            date.text = dateUtils.smartDate(dateUtils.dateFromMillis(item.date), false)
            if (item.hasNumberField) {
                tvNumber.text = item.numberField.toString()
            } else {
                tvNumber.visibility = View.GONE
            }
            if (item.hasTextField) {
                tvText.text = item.textField
            } else {
                tvText.visibility = View.GONE
            }

            if (!item.status) {
                Picasso.get().load(item.imageOff).into(ivImage)
                ivImage?.alpha = 0.4f
            } else {
                Picasso.get().load(item.imageOn).into(ivImage)
                ivImage?.alpha = 1f
            }
        }
    }


}