package com.sirionrazzer.diary.creator

import android.content.res.TypedArray
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.sirionrazzer.diary.R
import com.squareup.picasso.Picasso

class ImagePickerAdapter(
    private val names: Array<String>,
    private val ids: TypedArray,
    private val ticViewModel: TemplateItemCreatorViewModel,
    private val listener: ImagePickerDialog.ImagePickerDialogListener,
    private val fragment: DialogFragment
) :
    RecyclerView.Adapter<ImagePickerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.template_image, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.title.text = names[i]
        viewHolder.img.scaleType = ImageView.ScaleType.FIT_CENTER
        Picasso.get().load(ids.getResourceId(i, -1)).into(viewHolder.img)
        viewHolder.itemView.setOnClickListener {
            ticViewModel.setImageResource(ids.getResourceId(i, -1))
            listener.onImagePicked(fragment)
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val img: ImageView = view.findViewById(R.id.img)
    }
}