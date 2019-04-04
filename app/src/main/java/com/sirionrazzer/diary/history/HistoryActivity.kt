package com.sirionrazzer.diary.history

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.sirionrazzer.diary.R
import kotlinx.android.synthetic.main.toolbar.*
import com.sirionrazzer.diary.models.TrackItem

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbar)
        toolbar.title = ""

        val item1 = TrackItem("1", false, "item 1", 1, 1, false, false, true, null, null, 123)
        val item2 = TrackItem("2", false, "item 2", 1, 1, false, false, false, null, null, 123)
        val item3 = TrackItem("3", false, "item 3", 1, 1, false, false, true, null, null, 123)
        val item4 = TrackItem("4", false, "item 4", 1, 1, false, false, true, null, null, 123)
        val item5 = TrackItem("5", false, "item 5", 1, 1, false, false, true, null, null, 123)
        val item6 = TrackItem("6", false, "item 6", 1, 1, false, false, false, null, null, 123)
        val item7 = TrackItem("7", false, "item 7", 1, 1, false, false, true, null, null, 123)
        val item10 = TrackItem("10", false, "item 10", 1, 1, true, false, true, "some text", null, 123)
        val item11 = TrackItem("11", false, "item 11", 1, 1, true, false, false, "some text", null, 123)
        val item12 = TrackItem("12", false, "item 12", 1, 1, false, true, true, null, 42.toFloat(), 123)
        val trackitems1: ArrayList<TrackItem> = arrayListOf(item1, item2, item3, item4, item5, item6, item7, item10)
        val trackitems2: ArrayList<TrackItem> = arrayListOf(item1, item2, item3)
        val trackitems3: ArrayList<TrackItem> = arrayListOf(item4, item5, item6, item7, item10, item11, item12)
        val historyItems: ArrayList<ArrayList<TrackItem>> = arrayListOf(trackitems1, trackitems2, trackitems3)

        viewManager = LinearLayoutManager(this)
        viewAdapter = HistoryAdapter(this, historyItems)
        recyclerView = findViewById(R.id.historyItemRecyclerView)
        recyclerView.adapter = viewAdapter
        recyclerView.layoutManager = viewManager
    }
}
