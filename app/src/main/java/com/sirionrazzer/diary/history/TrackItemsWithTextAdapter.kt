package com.sirionrazzer.diary.history

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.squareup.picasso.Picasso

class TrackItemsWithTextAdapter(
    private val context: Context,
    private val trackItems: ArrayList<TrackItem>,
    private val clickListener: (String) -> Boolean
) : RecyclerView.Adapter<TrackItemsWithTextAdapter.ViewHolder>() {

    class ViewHolder(val templateItemLayout: LinearLayout) :
        RecyclerView.ViewHolder(templateItemLayout) {
        fun bind(trackItemName: String, clickListener: (String) -> Boolean) {
            templateItemLayout.setOnLongClickListener { clickListener(trackItemName) }
        }
    }

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackItemsWithTextAdapter.ViewHolder {

        val templateItemLayout =
            inflater.inflate(R.layout.template_item_with_text, parent, false) as LinearLayout

        return TrackItemsWithTextAdapter.ViewHolder(templateItemLayout)
    }

    override fun onBindViewHolder(holder: TrackItemsWithTextAdapter.ViewHolder, position: Int) {
        val trackItem = trackItems[position]
        val templateItemLayout = holder.templateItemLayout

        val nameTextView = templateItemLayout.findViewById<TextView>(R.id.trackItemName)
        nameTextView.text = trackItem.name
        val textTextView = templateItemLayout.findViewById<TextView>(R.id.tvTrackItemText)
        val imageImageView = templateItemLayout.findViewById<ImageView>(R.id.ivTrackItemImage)
        if (trackItem.status) {
            Picasso.get().load(trackItem.image).into(imageImageView)
            imageImageView?.alpha = 1f
            if (trackItem.hasTextField) {
                textTextView.text = trackItem.textField
            } else if (trackItem.hasNumberField) {
                textTextView.text = trackItem.numberField.toString()
            }
        } else {
            Picasso.get().load(trackItem.image).into(imageImageView)
            imageImageView?.alpha = 0.3f
        }

        holder.bind(trackItems[position].name, clickListener)
    }

    override fun getItemCount(): Int {
        return trackItems.size
    }
}
