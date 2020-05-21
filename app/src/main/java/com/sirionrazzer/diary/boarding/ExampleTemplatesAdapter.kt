package com.sirionrazzer.diary.boarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItemTemplate
import kotlinx.android.synthetic.main.example_item.view.*

class ExampleTemplatesAdapter(private val onCheckExampleTemplateListener: OnCheckExampleTemplateListener) :
    RecyclerView.Adapter<ExampleTemplatesAdapter.ViewHolder>() {

    private var items: List<TrackItemTemplate> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.example_item, parent, false)
        return ViewHolder(view)
    }

    fun submitList(items: List<TrackItemTemplate>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var name = itemView.tvName
        private var image = itemView.ivTemplate
        private var fieldImage = itemView.ivPencil
        private var description = itemView.item_description
        private var switch = itemView.item_switch

        fun bind(item: TrackItemTemplate) = with(itemView) {
            name.text = item.name
            image.setImageResource(item.image)
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
            description.text = item.description
            switch.isChecked = item.selected
            switch.setOnCheckedChangeListener { _, b ->
                onCheckExampleTemplateListener.onCheckExampleTemplate(layoutPosition, b)
            }
        }
    }
}