package com.sirionrazzer.diary.history

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem

class HistoryAdapter(private val context: Context, private val dataSource: ArrayList<ArrayList<TrackItem>>, private val clickListener: (Long) -> Unit) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val historyItemLayout: LinearLayout) : RecyclerView.ViewHolder(historyItemLayout) {
        fun bind(date: Long, clickListener: (Long) -> Unit) {
            historyItemLayout.setOnClickListener { clickListener(date) }
        }
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
            //= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): HistoryAdapter.ViewHolder {

        val historyItem = inflater.inflate(R.layout.history_item, parent, false) as LinearLayout

        return ViewHolder(historyItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = holder.historyItemLayout
        val gridLayout = historyItem.findViewById<GridView>(R.id.trackItemWithoutText)

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

        gridLayout.adapter = TrackItemsWithoutTextAdapter(gridLayout.context, trackItemsWithoutText)

        if (allTrackItems.size != 0) {
            holder.bind(allTrackItems[0].date, clickListener)
        }
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

}
