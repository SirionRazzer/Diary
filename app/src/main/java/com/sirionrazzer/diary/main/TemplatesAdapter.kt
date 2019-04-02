package com.sirionrazzer.diary.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItemTemplate

class TemplatesAdapter : BaseAdapter {

    lateinit var trackItemTemplateList: List<TrackItemTemplate>

    var context: Context

    constructor(context: Context, trackItemTemplateList: List<TrackItemTemplate>) : super() {
        this.context = context
        this.trackItemTemplateList = trackItemTemplateList
    }


    override fun getItem(position: Int): TrackItemTemplate {
        return trackItemTemplateList[position]
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getCount(): Int {
        return trackItemTemplateList.size
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = trackItemTemplateList[position]

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = inflator.inflate(R.layout.template_item, null)

        // TODO: fill with data
        //itemView.

        return itemView
    }

}