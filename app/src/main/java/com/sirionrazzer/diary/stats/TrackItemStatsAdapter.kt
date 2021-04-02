package com.sirionrazzer.diary.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.util.DateUtils

class TrackItemStatsAdapter(private val stats: List<TrackItem>) :
    RecyclerView.Adapter<TrackItemStatsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.stat_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return stats.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stats[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var date = itemView.findViewById<TextView>(R.id.tvStatItemDate)
        private var tvNumber = itemView.findViewById<TextView>(R.id.tvStatItemNumber)
        private var tvText = itemView.findViewById<TextView>(R.id.tvStatItemText)

        fun bind(item: TrackItem) {

            date.text = DateUtils.smartDate(DateUtils.dateFromMillis(item.date), false, true)
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
        }
    }
}