package com.sirionrazzer.diary.creator

import android.content.res.TypedArray
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sirionrazzer.diary.R

var imageIds: TypedArray? = null
var imageNames: Array<String>? = null

class ImagePickerDialog(creatorViewModel: TemplateItemCreatorViewModel) : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_image_picker, container, false)
        getDialog().setTitle("Choose icon")

        val dismiss = rootView.findViewById(R.id.dismiss) as Button
        dismiss.setOnClickListener { dismiss() }

        imageIds = resources.obtainTypedArray(R.array.img_template_drawables)
        imageNames = resources.getStringArray(R.array.img_template_names)

        val adapter = ImagePickerAdapter(imageNames!!, imageIds!!)
        val rvImages = rootView.findViewById(R.id.rvImages) as RecyclerView
        rvImages.layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
        rvImages.adapter = adapter

        return rootView
    }

    override fun onDestroy() {
        imageIds?.recycle()
        super.onDestroy()
    }
}
