package com.sirionrazzer.diary.history

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.stats.TrackItemStatsActivity
import com.sirionrazzer.diary.util.DateUtils

class HistoryAdapter(
    private val context: Context,
    private val dataSource: ArrayList<ArrayList<TrackItem>>)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private val dateUtils = DateUtils()

    class ViewHolder(val historyItemLayout: LinearLayout) : RecyclerView.ViewHolder(historyItemLayout)

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): HistoryAdapter.ViewHolder {

        val historyItem = inflater.inflate(R.layout.history_item, parent, false) as LinearLayout

        return ViewHolder(historyItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = holder.historyItemLayout
        val allTrackItems = dataSource[position]

        val gridLayout = historyItem.findViewById<GridView>(R.id.trackItemWithoutText)
        val recyclerView = historyItem.findViewById<RecyclerView>(R.id.trackItemWithText)

        val trackItemsWithoutText: ArrayList<TrackItem> = arrayListOf()
        val trackItemsWithText: ArrayList<TrackItem> = arrayListOf()
        val trackItemsIds: ArrayList<String> = arrayListOf()
        allTrackItems.forEach {
            trackItemsIds.add(it.id)
            if (it.hasTextField or it.hasNumberField) {
                trackItemsWithText.add(it)
            }
            else {
                trackItemsWithoutText.add(it)
            }
        }

        gridLayout.adapter = TrackItemsWithoutTextAdapter(gridLayout.context, trackItemsWithoutText)
        recyclerView.adapter = TrackItemsWithTextAdapter(recyclerView.context, trackItemsWithText) { trackItemName: String -> trackItemClicked(trackItemName) }
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)

        if (!allTrackItems.isEmpty()) {
            val date = historyItem.findViewById<TextView>(R.id.historyItemDate)
            date.text = dateUtils.smartDate(dateUtils.dateFromMillis(allTrackItems[0].date), false)

            val editButton: Button = historyItem.findViewById(R.id.btnEditHistoryItem)
            editButton.setOnClickListener {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("trackItemsIds", trackItemsIds)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    private fun trackItemClicked(trackItemName: String) {
        val intent = Intent(context, TrackItemStatsActivity::class.java)
        intent.putExtra("trackItemName", trackItemName)
        context.startActivity(intent)
    }
}
