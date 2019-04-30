package com.sirionrazzer.diary.creator

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_image_picker.*

var imageIds: TypedArray? = null
var imageNames: Array<String>? = null

class ImagePickerDialog(val creatorViewModel: TemplateItemCreatorViewModel) : DialogFragment() {

    public lateinit var listener: ImagePickerDialogListener

    interface ImagePickerDialogListener {
        fun onImagePicked(dialog: DialogFragment)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ImagePickerDialogListener so we can send events to the host
            listener = context as ImagePickerDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement ImagePickerDialogListener")
            )
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_image_picker, container, false)
        getDialog().setTitle("Choose icon")

        imageIds = resources.obtainTypedArray(R.array.img_template_drawables)
        imageNames = resources.getStringArray(R.array.img_template_names)

        val adapter = ImagePickerAdapter(imageNames!!, imageIds!!, creatorViewModel, listener, this)
        val rvImages = rootView.findViewById(R.id.rvImages) as RecyclerView
        rvImages.layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
        rvImages.adapter = adapter

        return rootView
    }


    override fun onResume() {
        super.onResume()
        val params = dialog.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams

        btnDismiss.setOnClickListener { dismiss() }
    }


    override fun onDestroy() {
        imageIds?.recycle()
        super.onDestroy()
    }
}
