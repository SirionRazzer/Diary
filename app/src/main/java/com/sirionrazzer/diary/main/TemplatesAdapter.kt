package com.sirionrazzer.diary.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.template_item.view.*

class TemplatesAdapter(private val context: Context, private val mainViewModel: MainViewModel) : BaseAdapter() {

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
            Picasso.get().load(mainViewModel.currentTrackItems[position].imageOff).into(holder.ivImage)
            holder.ivImage?.alpha = 0.4f
            holder.tvName?.text = mainViewModel.currentTrackItems[position].name

            itemView.setOnClickListener {
                if (holder.status == false) {
                    Picasso.get().load(mainViewModel.currentTrackItems[position].imageOn).into(holder.ivImage)
                    mainViewModel.currentTrackItems[position].status = true
                    holder.status = true
                    holder.ivImage?.alpha = 1.0f
                    Log.d("TemplatesAdapter", "Clicked: " + position + ". track item, the state was false and now is " + holder.status.toString())
                } else {
                    Picasso.get().load(mainViewModel.currentTrackItems[position].imageOff).into(holder.ivImage)
                    mainViewModel.currentTrackItems[position].status = false
                    holder.status = false
                    holder.ivImage?.alpha = 0.4f
                    Log.d("TemplatesAdapter", "Clicked: " + position + ". track item, the state was true and now is " + holder.status.toString())
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