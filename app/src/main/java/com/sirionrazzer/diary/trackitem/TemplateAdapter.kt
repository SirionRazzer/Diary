package com.sirionrazzer.diary.trackitem

import android.util.Log
import android.view.View
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.viewer_template_item.view.*

class TemplateAdapter(
    currentTemplateItems: MutableList<TrackItemTemplate>,
    val tiViewModel: TemplateItemViewerViewModel
) :
    DragDropSwipeAdapter<TrackItemTemplate, TemplateAdapter.ViewHolder>(currentTemplateItems) {

    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        var ivImage = itemView.ivTemplate
        var ivPencil = itemView.ivPencil
        var tvName = itemView.tvName
        var swDeleted = itemView.swDeleted
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView)

    override fun onBindViewHolder(item: TrackItemTemplate, viewHolder: ViewHolder, position: Int) {
        // Here we update the contents of the view holder's views to reflect the item's data
        //viewHolder.itemText.text = item
        Picasso.get().load(item.imageOn).into(viewHolder.ivImage)
        //Picasso.get().load(R.drawable.ic_edit_badge).into(ivPencil)
        if (item.hasNumberField ||
            item.hasTextField
        ) {
            viewHolder.ivPencil.visibility = View.VISIBLE
            viewHolder.ivPencil.setImageResource(R.drawable.ic_edit_badge)
        } else {
            viewHolder.ivPencil.visibility = View.INVISIBLE
        }
        viewHolder.tvName.text = item.name
        viewHolder.swDeleted.isChecked = !item.deleted

//        itemView.setOnClickListener {
//            updateTemplate(tiViewModel, position, !swDeleted.isChecked)
//            swDeleted.isChecked = !swDeleted.isChecked
//        }
//
        viewHolder.swDeleted.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (compoundButton.isShown) {
                updateTemplate(item, isChecked)
                Log.d("FAKE CLICK", " I SEE YOU SUCKER!")
            }
        }
    }

    override fun getViewToTouchToStartDraggingItem(
        item: TrackItemTemplate,
        viewHolder: ViewHolder,
        position: Int
    ): View? {
        // We return the view holder's view on which the user has to touch to drag the item
        return viewHolder.ivImage
    }

    private fun updateTemplate(item: TrackItemTemplate, isChecked: Boolean) {
        var template = TrackItemTemplate()
        template.id = item.id
        template.name = item.name
        template.imageOn = item.imageOn
        template.imageOff = item.imageOff
        template.position = item.position
        template.hasTextField = item.hasTextField
        template.hasNumberField = item.hasNumberField
        template.deleted = !isChecked // <-- this

        tiViewModel.updateTemplate(template)
        tiViewModel.refreshTemplateList()
        tiViewModel.hasChanged = true
    }


}