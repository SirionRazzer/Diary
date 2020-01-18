package com.sirionrazzer.diary.viewer

import android.content.Context
import android.view.View
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.template_item.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class TemplateAdapter(
    currentTemplateItems: MutableList<TrackItemTemplate>,
    val tiViewModel: TemplateItemViewerViewModel,
    val context: Context
) :
    DragDropSwipeAdapter<TrackItemTemplate, TemplateAdapter.ViewHolder>(currentTemplateItems) {

    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        var ivImage = itemView.ivTemplate
        var ivPencil = itemView.ivPencil
        var tvName = itemView.tvName
        //var swDeleted = itemView.swDeleted
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView)

    override fun onBindViewHolder(item: TrackItemTemplate, viewHolder: ViewHolder, position: Int) {
        // Here we update the contents of the view holder's views to reflect the item's data
        Picasso.get().load(item.image).into(viewHolder.ivImage)
        val fieldImage = viewHolder.ivPencil
        when {
            item.hasTextField -> {
                fieldImage.visibility = View.VISIBLE
                fieldImage.setImageResource(R.drawable.ic_edit_badge)
            }
            item.hasNumberField -> {
                fieldImage.visibility = View.VISIBLE
                fieldImage.setImageResource(R.drawable.ic_edit_badge_2)
            }
            else -> {
                fieldImage.visibility = View.GONE
            }
        }
        viewHolder.tvName.text = item.name

        viewHolder.ivImage.setOnLongClickListener {
            context.alert(
                "Delete this activity?",
                "Your previous records won't be removed"
            ) {
                yesButton {
                    deleteTemplate(item)
                }
                noButton {
                    it.cancel()
                }
            }.show()
            false
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

    private fun deleteTemplate(item: TrackItemTemplate) {
        tiViewModel.deleteTemplate(item)
        refresh()
    }

    fun refresh() {
        super.dataSet = tiViewModel.currentTemplateItems.value ?: mutableListOf()
        super.notifyDataSetChanged()
    }
}