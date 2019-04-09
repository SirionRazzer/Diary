package com.sirionrazzer.diary.trackitem

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.viewer_template_item.view.*

class TemplateViewerAdapter(private val context: Context, private val tiViewModel: TemplateItemViewerViewModel) : RecyclerView.Adapter<TemplateViewerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.viewer_template_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(tiViewModel.currentTemplateItems[position])
    }

    override fun getItemCount(): Int {
        return tiViewModel.currentTemplateItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivImage = itemView.ivTemplate
        var tvName = itemView.tvName
        var swDeleted = itemView.swDeleted

        fun bindItems(trackItemTemplate: TrackItemTemplate) {
            Picasso.get().load(trackItemTemplate.imageOn).into(ivImage)
            tvName.text = trackItemTemplate.name
            swDeleted.isChecked = trackItemTemplate.deleted

            swDeleted.setOnCheckedChangeListener { _, isChecked ->
                trackItemTemplate.deleted = !isChecked
            }
        }
    }

}
