package com.sirionrazzer.diary.history

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem

class TrackItemsWithoutTextAdapter(private val context: Context, private val trackItems: ArrayList<TrackItem>) : BaseAdapter() {

    class ViewHolder(val nameTextView: TextView) : RecyclerView.ViewHolder(nameTextView)

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return trackItems.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItem(position: Int): TrackItem {
        return trackItems[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var templateItemView = convertView
        val holder: ViewHolder

        if (templateItemView == null) {
            templateItemView = inflater.inflate(R.layout.template_item, parent, false)
            holder = ViewHolder(templateItemView!!.findViewById(R.id.trackitemName) as TextView)
            templateItemView.tag = holder
        }
        else {
            holder = templateItemView.tag as ViewHolder
        }
        val trackItem = getItem(position)
        holder.nameTextView.text = trackItem.name

        return templateItemView
    }

}
