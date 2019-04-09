package com.sirionrazzer.diary.history

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.stats.TrackItemStatsActivity

class HistoryAdapter(
    private val context: Context,
    private val dataSource: ArrayList<ArrayList<TrackItem>>,
    private val clickListener: (ArrayList<String>) -> Unit)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val historyItemLayout: LinearLayout) : RecyclerView.ViewHolder(historyItemLayout) {
        fun bind(trackItemsIds: ArrayList<String>, clickListener: (ArrayList<String>) -> Unit) {
            historyItemLayout.setOnClickListener { clickListener(trackItemsIds) }
        }
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): HistoryAdapter.ViewHolder {

        val historyItem = inflater.inflate(R.layout.history_item, parent, false) as LinearLayout

        return ViewHolder(historyItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = holder.historyItemLayout
        val gridLayout = historyItem.findViewById<GridView>(R.id.trackItemWithoutText)
        val recyclerView = historyItem.findViewById<RecyclerView>(R.id.trackItemWithText)

        val allTrackItems = dataSource[position]
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
//        Toast.makeText(context, trackItemsWithText.size.toString(), Toast.LENGTH_LONG).show()
        gridLayout.adapter = TrackItemsWithoutTextAdapter(gridLayout.context, trackItemsWithoutText)
        recyclerView.adapter = TrackItemsWithTextAdapter(recyclerView.context, trackItemsWithText) { trackItemName: String -> trackItemClicked(trackItemName) }
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)

        holder.bind(trackItemsIds, clickListener)
//        val intent = Intent(historyItem.context, MainActivity::class.java)
//        intent.putExtra("date", allTrackItems[0].date)
//        historyItem.setOnClickListener {
//            Toast.makeText(context, "a", Toast.LENGTH_LONG).show()
//            context.startActivity(intent)
//        }
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
