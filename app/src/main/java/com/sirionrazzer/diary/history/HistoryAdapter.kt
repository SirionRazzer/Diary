package com.sirionrazzer.diary.history

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem

class HistoryAdapter(private val context: Context, private val dataSource: ArrayList<ArrayList<TrackItem>>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val historyItemLayout: LinearLayout) : RecyclerView.ViewHolder(historyItemLayout)

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): HistoryAdapter.ViewHolder {

        val historyItem = inflater.inflate(R.layout.history_item, parent, false) as LinearLayout

        return ViewHolder(historyItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = holder.historyItemLayout
        val gridLayout = historyItem.findViewById(R.id.trackItemWithoutText) as GridView

        val allTrackItems = dataSource[position]
        val trackItemsWithoutText: ArrayList<TrackItem> = arrayListOf()
        val trackItemsWithText: ArrayList<TrackItem> = arrayListOf()
        allTrackItems.forEach {
            if (it.hasTextField or it.hasNumberField) {
                trackItemsWithText.add(it)
            }
            else {
                trackItemsWithoutText.add(it)
            }
        }

        gridLayout.adapter = TrackItemsWithoutTextAdapter(gridLayout.context, trackItemsWithoutText)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

}
