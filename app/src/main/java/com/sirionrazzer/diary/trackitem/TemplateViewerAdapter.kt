package com.sirionrazzer.diary.trackitem

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
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
        var ivPencil = itemView.ivPencil
        var tvName = itemView.tvName
        var swDeleted = itemView.swDeleted

        fun bindItems(tiViewModel: TemplateItemViewerViewModel, position: Int) {
            Picasso.get().load(tiViewModel.currentTemplateItems[position].imageOn).into(ivImage)
            //Picasso.get().load(R.drawable.ic_edit_badge).into(ivPencil)
            if (tiViewModel.currentTemplateItems[position].hasNumberField ||
                tiViewModel.currentTemplateItems[position].hasTextField
            ) {
                ivPencil.visibility = View.VISIBLE
                ivPencil.setImageResource(R.drawable.ic_edit_badge)
            } else {
                ivPencil.visibility = View.INVISIBLE
            }
            tvName.text = tiViewModel.currentTemplateItems[position].name
            swDeleted.isChecked = !tiViewModel.currentTemplateItems[position].deleted

            itemView.setOnClickListener {
                updateTemplate(tiViewModel, position, !swDeleted.isChecked)
                swDeleted.isChecked = !swDeleted.isChecked
            }

            swDeleted.setOnCheckedChangeListener { _, isChecked ->
                updateTemplate(tiViewModel, position, isChecked)
            }
        }

        private fun updateTemplate(tiViewModel: TemplateItemViewerViewModel, position: Int, isChecked: Boolean) {
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
