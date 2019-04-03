package com.sirionrazzer.diary.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.models.TrackItemTemplate
import kotlinx.android.synthetic.main.template_item.view.*

class TemplatesAdapter(private val context: Context) : BaseAdapter() {

    lateinit var mainViewModel: MainViewModel

    override fun getItem(position: Int): TrackItem {
        return mainViewModel.currentTrackItems[position]
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getCount(): Int {
        return mainViewModel.currentTrackItems.size
    }


    override fun getView(position: Int, itemView: View?, parent: ViewGroup?): View {
        var itemView = itemView
        val holder: ViewHolder

        if (itemView == null) {
            holder = ViewHolder()

            var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = inflater.inflate(R.layout.template_item, null, true)

            holder.ivImage = itemView!!.trackitemImage as ImageView
            holder.tvName = itemView!!.trackitemName as TextView

            itemView.tag = holder
        } else {
            holder = itemView.tag as ViewHolder
        }

        if (!mainViewModel.currentTrackItems[position].deleted) {
            holder.ivImage?.setImageResource(mainViewModel.currentTrackItems[position].imageOff)
            holder.tvName?.text = mainViewModel.currentTrackItems[position].name

            itemView.setOnClickListener {
                if (holder.status == false) {
                    holder.ivImage?.setImageResource(mainViewModel.currentTrackItems[position].imageOn)
                    mainViewModel.currentTrackItems[position].status = true
                } else {
                    holder.ivImage?.setImageResource(mainViewModel.currentTrackItems[position].imageOff)
                    mainViewModel.currentTrackItems[position].status = false
                }

                // TODO: handle input text/number fields
            }
        } else {
            itemView.visibility = View.GONE
        }

        return itemView
    }

    private inner class ViewHolder {
        var tvName: TextView? = null
        var ivImage: ImageView? = null
        internal var status: Boolean = false
    }
}