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

class TemplateViewerAdapter(private val context: Context, private val tiViewModel: TemplateItemViewerViewModel) :
    RecyclerView.Adapter<TemplateViewerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.viewer_template_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(tiViewModel, position)
    }

    override fun getItemCount(): Int {
        return tiViewModel.currentTemplateItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivImage = itemView.ivTemplate
        var tvName = itemView.tvName
        var swDeleted = itemView.swDeleted

        fun bindItems(tiViewModel: TemplateItemViewerViewModel, position: Int) {
            Picasso.get().load(tiViewModel.currentTemplateItems[position].imageOn).into(ivImage)
            tvName.text = tiViewModel.currentTemplateItems[position].name
            swDeleted.isChecked = !tiViewModel.currentTemplateItems[position].deleted

            swDeleted.setOnCheckedChangeListener { _, isChecked ->
                var template = TrackItemTemplate()
                template.id = tiViewModel.currentTemplateItems[position].id
                template.name = tiViewModel.currentTemplateItems[position].name
                template.imageOn = tiViewModel.currentTemplateItems[position].imageOn
                template.imageOff = tiViewModel.currentTemplateItems[position].imageOff
                template.position = tiViewModel.currentTemplateItems[position].position
                template.hasTextField = tiViewModel.currentTemplateItems[position].hasTextField
                template.hasNumberField = tiViewModel.currentTemplateItems[position].hasNumberField
                template.deleted = !isChecked // <-- this

                tiViewModel.updateTemplate(template)
                tiViewModel.hasChanged = true
            }
        }
    }
}
