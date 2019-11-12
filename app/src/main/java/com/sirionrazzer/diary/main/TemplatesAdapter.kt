package com.sirionrazzer.diary.main

import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.template_item.view.*
import androidx.databinding.ObservableField
import org.jetbrains.anko.colorAttr

class TemplatesAdapter(private val context: Context, private val mainViewModel: MainViewModel) : BaseAdapter() {

    override fun getItem(position: Int): TrackItem {
        return mainViewModel.currentTrackItems[position]
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getCount(): Int {
        return mainViewModel.currentTrackItems.size
    }


    override fun getView(position: Int, itemView: View?, parent: ViewGroup?): View {
        var itemView = itemView
        val holder: ViewHolder

        if (itemView == null) {
            holder = ViewHolder()

            var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = inflater.inflate(R.layout.template_item, null, true)

            holder.ivImage = itemView!!.trackitemImage as ImageView
            holder.tvName = itemView!!.trackitemName as TextView
            holder.ivPencil = itemView!!.ivPencil as ImageView

            itemView.tag = holder
        } else {
            holder = itemView.tag as ViewHolder
        }

        if (!mainViewModel.currentTrackItems[position].deleted) {

            holder.status = mainViewModel.currentTrackItems[position].status
            Log.d("TemplatesAdapter", "position: " + position + " holder.status: " + holder.status.toString())

            holder.tvName?.text = mainViewModel.currentTrackItems[position].name

            if (mainViewModel.currentTemplateItems[position].hasTextField ||
                mainViewModel.currentTemplateItems[position].hasNumberField) {
                holder.ivPencil?.visibility = View.VISIBLE
                //Picasso.get().load(R.drawable.ic_pencil).into(holder.ivPencil)
                holder.ivPencil?.setImageResource(R.drawable.ic_edit_badge)
            } else {
                holder.ivPencil?.visibility = View.INVISIBLE
            }

            if (!mainViewModel.currentTrackItems[position].status) {
                Picasso.get().load(mainViewModel.currentTrackItems[position].image).into(holder.ivImage)
                holder.ivImage?.alpha = 0.4f
                holder.tvName?.setTextColor(context.resources.getColor(R.color.colorPrimary))
            } else {
                Picasso.get().load(mainViewModel.currentTrackItems[position].image).into(holder.ivImage)
                holder.ivImage?.alpha = 1f
                holder.tvName?.setTextColor(context.resources.getColor(R.color.colorPrimary))
            }

            itemView.setOnClickListener {
                if (!holder.status) {

                    when {
                        mainViewModel.currentTemplateItems[position].hasTextField -> {
                            showDialogWithTextInput(position, mainViewModel.currentTrackItems[position].name, holder)
                        }
                        mainViewModel.currentTemplateItems[position].hasNumberField -> {
                            showDialogWithNumberInput(position, mainViewModel.currentTrackItems[position].name, holder)
                        }
                        else -> enableItemStatus(position, holder)
                    }


                } else {
                    Picasso.get().load(mainViewModel.currentTrackItems[position].image).into(holder.ivImage)
                    holder.status = false
                    holder.ivImage?.alpha = 0.4f
                    holder.tvName?.setTextColor(context.resources.getColor(R.color.colorPrimary))
                    Log.d(
                        "TemplatesAdapter",
                        "Clicked: " + position + ". track item, the state was true and now is " + holder.status.toString()
                    )
                    mainViewModel.currentTrackItems[position].status = false
                    mainViewModel.currentTrackItems[position].hasTextField = false
                    mainViewModel.currentTrackItems[position].hasNumberField = false
                    mainViewModel.currentTrackItems[position].textField = ""
                    mainViewModel.currentTrackItems[position].numberField = 0f
                }
            }
        } else {
            itemView.visibility = View.GONE
        }

        return itemView
    }


    private fun enableItemStatus(position: Int, holder: ViewHolder) {
        Picasso.get().load(mainViewModel.currentTrackItems[position].image).into(holder.ivImage)
        holder.status = true
        holder.ivImage?.alpha = 1.0f
        holder.tvName?.setTextColor(context.resources.getColor(R.color.colorPrimary))
        Log.d(
            "TemplatesAdapter",
            "Clicked: " + position + ". track item, the state was false and now is " + holder.status.toString()
        )

        mainViewModel.currentTrackItems[position].status = true
    }


    private inner class ViewHolder {
        var tvName: TextView? = null
        var ivImage: ImageView? = null
        var ivPencil: ImageView? = null
        internal var status: Boolean = false
    }


    private fun showDialogWithTextInput(itemPosition: Int, headerText: String, holder: ViewHolder) {
        val textInputLayout = TextInputLayout(context)
        textInputLayout.setPadding(
            19,
            0,
            19,
            0
        )
        val input = EditText(context)
        textInputLayout.addView(input)

        val alert = AlertDialog.Builder(context)
            .setTitle(context.resources.getString(R.string.activity_note_first_part) + headerText + context.resources.getString(R.string.activity_note_second_part))
            .setView(textInputLayout)
            .setPositiveButton(context.resources.getString(R.string.submit)) { dialog, _ ->

                mainViewModel.currentTrackItems[itemPosition].textField = input.text.toString()
                mainViewModel.currentTrackItems[itemPosition].hasTextField = true
                enableItemStatus(itemPosition, holder)

                dialog.cancel()
            }
            .setNegativeButton(context.resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.create()

        alert.show()
    }

    private fun showDialogWithNumberInput(itemPosition: Int, headerText: String, holder: ViewHolder) {
        val textInputLayout = TextInputLayout(context)
        textInputLayout.setPadding(
            19,
            0,
            19,
            0
        )
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        textInputLayout.addView(input)

        val alert = AlertDialog.Builder(context)
            .setTitle(context.resources.getString(R.string.activity_note_first_part) + headerText + context.resources.getString(R.string.activity_note_second_part))
            .setView(textInputLayout)
            .setPositiveButton(context.resources.getString(R.string.submit)) { dialog, _ ->

                val stringNum = input.text.toString()
                mainViewModel.currentTrackItems[itemPosition].hasNumberField = true
                if (stringNum.isNotEmpty()) {
                    mainViewModel.currentTrackItems[itemPosition].numberField = stringNum.toFloat()
                } else {
                    mainViewModel.currentTrackItems[itemPosition].numberField = 0f
                }
                enableItemStatus(itemPosition, holder)

                dialog.cancel()
            }
            .setNegativeButton(context.resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.create()

        alert.show()
    }
}
