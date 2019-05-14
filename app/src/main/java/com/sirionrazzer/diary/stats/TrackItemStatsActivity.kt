package com.sirionrazzer.diary.stats

import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.models.TrackItemDao
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.sirionrazzer.diary.models.TrackItemTemplateDao
import com.sirionrazzer.diary.util.DateUtils
import com.squareup.picasso.Picasso
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_trackitem_stats.*
import kotlinx.android.synthetic.main.toolbar.*

class TrackItemStatsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    var currentTrackItems: MutableList<TrackItem> = mutableListOf()
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter: TrackItemStatsAdapter
    private lateinit var template: TrackItemTemplate

    var barData: MutableList<Pair<Float, String>> = mutableListOf()

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trackitem_stats)

        val trackItemName = intent.getStringExtra("trackItemName")

        toolbar.setTitle(R.string.title_stats_activity)
        setSupportActionBar(toolbar)
        template = realm.trackItemsTemplatesDao.getTemplateByName(trackItemName)!!
        tvTemplateStatName.text = template.name
        Picasso.get().load(template.imageOn).into(ivTemplateStatImage)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initSpinners()
        initTrackAndTemplateItems(trackItemName)

        initRecyclerView()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private fun initSpinners() {
        ArrayAdapter.createFromResource(
            this,
            R.array.time_unit_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            timeUnitSpinner.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.stat_group_by_operations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            statGroupBySpinner.adapter = adapter
        }

    }

    private fun initBarChart() {
        barChart.setDrawGridBackground(false)

        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.valueFormatter = XDateFormatter(barChart, barData, R.string.week, this)
        barChart.axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisLeft.spaceTop = 15f
        barChart.axisLeft.valueFormatter = StringFormatter(barChart)

        barChart.axisRight.isEnabled = false

        setData()
    }

    private fun getTrackItemsGroupedByTimeUnit(): Map<String, MutableList<TrackItem>> {

        val map = HashMap<String, MutableList<TrackItem>>()
        currentTrackItems.forEach {

            val pair = DateUtils.getWeekAndYearPair(it.date)
            val key = DateUtils.getWeekYearString(pair)
            if (!map.containsKey(key)) {
                map[key] = mutableListOf()
            }
            map[key]!!.add(it)

        }
        return map
    }

    private fun setData() {
        val values = mutableListOf<BarEntry>()

        barData.forEachIndexed { index, pair ->
            values.add(BarEntry(index.toFloat(), pair.first))
        }
        val dataSet = BarDataSet(values, "${template.name} ${getString(R.string.average_each_week)}")
        dataSet.setDrawIcons(false)

        val data = BarData(mutableListOf<IBarDataSet>(dataSet))
        data.setValueTextSize(10f)

        data.setValueTypeface(Typeface.DEFAULT)
        data.barWidth = 0.8f
        barChart.data = data
    }

    private fun initTrackAndTemplateItems(name: String) {
        currentTrackItems.addAll(realm.trackItemsDao.getTrackItemsWithName(name))

        if (template.hasNumberField) {
            val itemsGrouped = getTrackItemsGroupedByTimeUnit()

            itemsGrouped.forEach { (key, value) ->
                var sum = 0f
                value.forEach {
                    sum += it.numberField!!
                }

                barData.add(Pair(sum / value.count(), key))
            }
            barData.sortBy { it.second }
            initBarChart()

        } else {
            barChart.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = TrackItemStatsAdapter(currentTrackItems)
        rvTrackItemStats.adapter = viewAdapter
        rvTrackItemStats.layoutManager = viewManager
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

}

private val Realm.trackItemsDao: TrackItemDao
    get() {
        return TrackItemDao(this)
    }
private val Realm.trackItemsTemplatesDao: TrackItemTemplateDao
    get() {
        return TrackItemTemplateDao(this)
    }

