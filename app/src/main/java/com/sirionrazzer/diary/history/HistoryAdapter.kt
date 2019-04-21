package com.sirionrazzer.diary.history

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.stats.TrackItemStatsActivity

class HistoryAdapter(
    private val context: Context,
    private val historyViewModel: HistoryViewModel)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val historyItemLayout: LinearLayout) : RecyclerView.ViewHolder(historyItemLayout)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): HistoryAdapter.ViewHolder {

        val historyItem = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false) as LinearLayout

        return ViewHolder(historyItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = holder.historyItemLayout
        val date = historyViewModel.dates[position]
        val allTrackItems = historyViewModel.trackItemsByDate[date]

        val gridLayout = historyItem.findViewById<GridView>(R.id.trackItemWithoutText)
        val recyclerView = historyItem.findViewById<RecyclerView>(R.id.trackItemWithText)

        val trackItemsWithoutText: ArrayList<TrackItem> = arrayListOf()
        val trackItemsWithText: ArrayList<TrackItem> = arrayListOf()
        val trackItemsIds: ArrayList<String> = arrayListOf()
        allTrackItems!!.forEach {
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

        val dateTextView = historyItem.findViewById<TextView>(R.id.historyItemDate)
        dateTextView.text = date

        val editButton: ImageButton = historyItem.findViewById(R.id.btnEditHistoryItem)
        editButton.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("trackItemsIds", trackItemsIds)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return historyViewModel.dates.size
    }

    private fun trackItemClicked(trackItemName: String) {
        val intent = Intent(context, TrackItemStatsActivity::class.java)
        intent.putExtra("trackItemName", trackItemName)
        context.startActivity(intent)
    }
}
