package com.sirionrazzer.diary.history

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.stats.TrackItemStatsActivity
import com.squareup.picasso.Picasso

class TrackItemsWithoutTextAdapter(context: Context, private val trackItems: ArrayList<TrackItem>) : BaseAdapter() {

    class ViewHolder(val nameTextView: TextView, val image: ImageView) : RecyclerView.ViewHolder(nameTextView)

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return trackItems.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): TrackItem {
        return trackItems[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var templateItemView = convertView
        val holder: ViewHolder

        if (templateItemView == null) {
            templateItemView = inflater.inflate(R.layout.template_item, parent, false)
            holder = ViewHolder(templateItemView!!.findViewById(R.id.trackitemName) as TextView,
                templateItemView.findViewById(R.id.trackitemImage) as ImageView)
            templateItemView.tag = holder
        }
        else {
            holder = templateItemView.tag as ViewHolder
        }
        val trackItem = getItem(position)
        holder.nameTextView.text = trackItem.name
        if (trackItem.status) {
            Picasso.get().load(trackItem.image).into(holder.image)
            holder.image.alpha = 1f
        } else {
            Picasso.get().load(trackItem.image).into(holder.image)
            holder.image.alpha = 0.3f
        }

        val intent = Intent(templateItemView.context, TrackItemStatsActivity::class.java)
            .putExtra("trackItemName", trackItem.name)
        templateItemView.setOnLongClickListener {
            templateItemView.context.startActivity(intent)
            true
        }

        return templateItemView
    }

}
