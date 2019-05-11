package com.sirionrazzer.diary.stats


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.main.MainViewModel
import com.sirionrazzer.diary.models.TrackItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.template_item.view.*


class TemplatesStatsAdapter(private val context: Context, private val mainViewModel: MainViewModel) : BaseAdapter() {

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
        var view = itemView
        val holder: ViewHolder

        if (view == null) {
            holder = ViewHolder()

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.template_item, null, true)

            holder.ivImage = view!!.trackitemImage as ImageView
            holder.tvName = view.trackitemName as TextView

            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        holder.tvName?.text = mainViewModel.currentTrackItems[position].name
        Picasso.get().load(mainViewModel.currentTrackItems[position].imageOn).into(holder.ivImage)
        holder.ivImage?.alpha = 1f
        holder.tvName?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))

        view.setOnClickListener {
            val intent = Intent(this.context, TrackItemStatsActivity::class.java)
                .putExtra("trackItemName", mainViewModel.currentTrackItems[position].name)
            view.context.startActivity(intent)
        }
        return view
    }

    private inner class ViewHolder {
        var tvName: TextView? = null
        var ivImage: ImageView? = null
        internal var status: Boolean = false
    }
}
