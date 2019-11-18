package com.sirionrazzer.diary.history

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.stats.TrackItemStatsActivity

class HistoryAdapter(
    private val context: Context,
    private val historyViewModel: HistoryViewModel
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val historyItemLayout: LinearLayout) :
        RecyclerView.ViewHolder(historyItemLayout)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val historyItem = LayoutInflater.from(context).inflate(
            R.layout.history_item,
            parent,
            false
        ) as LinearLayout

        return ViewHolder(historyItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = holder.historyItemLayout
        val date = historyViewModel.dates[position]
        val allTrackItems = historyViewModel.trackItemsByDate[date]
        val dateLong = allTrackItems?.first()?.date

        val gridLayout = historyItem.findViewById<GridView>(R.id.trackItemWithoutText)
        val recyclerView = historyItem.findViewById<RecyclerView>(R.id.trackItemWithText)

        val trackItemsWithoutText: ArrayList<TrackItem> = arrayListOf()
        val trackItemsWithText: ArrayList<TrackItem> = arrayListOf()
        val trackItemsIds: ArrayList<String> = arrayListOf()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val showOnlyFilled = prefs.getBoolean("show_only_filled_items", false)

        allTrackItems!!.forEach {
            if (it.status || !showOnlyFilled) {
                trackItemsIds.add(it.id)
                if (it.status and (it.hasTextField or it.hasNumberField)) {
                    trackItemsWithText.add(it)
                } else {
                    trackItemsWithoutText.add(it)
                }

            }
        }

        gridLayout.adapter = TrackItemsWithoutTextAdapter(gridLayout.context, trackItemsWithoutText)
        recyclerView.adapter = TrackItemsWithTextAdapter(
            recyclerView.context,
            trackItemsWithText
        ) { trackItemName: String -> trackItemClicked(trackItemName) }
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)

        val dateTextView = historyItem.findViewById<TextView>(R.id.historyItemDate)
        dateTextView.text = date

        val editButton: ImageButton = historyItem.findViewById(R.id.btnEditHistoryItem)
        editButton.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            dateLong?.let { intent.putExtra("editDate", it) }
            intent.putExtra("trackItemsIds", trackItemsIds)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return historyViewModel.dates.size
    }

    private fun trackItemClicked(trackItemName: String): Boolean {
        val intent = Intent(context, TrackItemStatsActivity::class.java)
        intent.putExtra("trackItemName", trackItemName)
        context.startActivity(intent)
        return true
    }
}
