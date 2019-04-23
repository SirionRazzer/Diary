package com.sirionrazzer.diary.creator

import android.content.res.TypedArray
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.sirionrazzer.diary.R
import com.squareup.picasso.Picasso


class ImagePickerAdapter(
    private val names: Array<String>,
    private val ids: TypedArray
) :
    RecyclerView.Adapter<ImagePickerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.template_image, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.title.text = names[i]
        viewHolder.img.scaleType = ImageView.ScaleType.CENTER_CROP
        Picasso.get().load(ids.getResourceId(i, -1)).into(viewHolder.img)
    }


    override fun getItemCount(): Int {
        return names.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val img: ImageButton = view.findViewById(R.id.img)
    }
}