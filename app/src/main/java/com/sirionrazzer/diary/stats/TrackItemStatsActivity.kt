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

class TrackItemStatsActivity : AppCompatActivity() {


    var currentTrackItems: MutableList<TrackItem> = mutableListOf()
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter: TrackItemStatsAdapter
    private lateinit var template: TrackItemTemplate
    private var selectedTimeUnit: Int = 0
    private var selectedGroupByOperation: Int = 0

    private var barData: MutableList<Pair<Float, String>> = mutableListOf()

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

        currentTrackItems.addAll(realm.trackItemsDao.getTrackItemsWithName(trackItemName))

        if (template.hasNumberField) {
            val sum = currentTrackItems.sumByDouble { it.numberField!!.toDouble() }
            val average = sum / currentTrackItems.count()
            tvStatAverageNumber.text = String.format("%.2f", average)
            tvStatSumNumber.text = String.format("%.2f", sum)

            initBarChart()
            refreshChartData()

        } else {
            barChart.visibility = View.GONE
        }
        initSpinners()

        initRecyclerView()

    }


    private fun initSpinners() {
        ArrayAdapter.createFromResource(
            this,
            R.array.time_unit_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            timeUnitSpinner.adapter = adapter
        }
        timeUnitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedTimeUnit = position
                refreshChartData()

            }

        }
        ArrayAdapter.createFromResource(
            this,
            R.array.stat_group_by_operations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statGroupBySpinner.adapter = adapter

        }
        statGroupBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedGroupByOperation = position
                refreshChartData()
            }

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
        barChart.axisLeft.valueFormatter = StringFormatter()
        barChart.axisRight.isEnabled = false
        barChart.description = null

    }

    private fun refreshChartData() {
        barData = mutableListOf()
        getTrackItemsGroupedByTimeUnit().forEach { (key, value) ->
            var sum = 0f
            value.forEach {
                sum += it.numberField!!
            }
            if (selectedGroupByOperation == 0) {
                barData.add(Pair(sum / value.count(), key))
            } else {
                barData.add(Pair(sum, key))
            }

        }
        barData.sortBy { it.second }

        val values = mutableListOf<BarEntry>()

        barData.forEachIndexed { index, pair ->
            values.add(BarEntry(index.toFloat(), pair.first))
        }
        var label = "${template.name} "

        if (selectedGroupByOperation == 0 && selectedTimeUnit == 0) {
            label += getString(R.string.average_each_week)
        } else if (selectedGroupByOperation == 0 && selectedTimeUnit == 1) {
            label += getString(R.string.average_each_month)
        } else if (selectedGroupByOperation == 1 && selectedTimeUnit == 0) {
            label += getString(R.string.sum_each_week)

        } else if (selectedGroupByOperation == 1 && selectedTimeUnit == 1) {
            label += getString(R.string.sum_each_month)
        }


        val dataSet = BarDataSet(values, label)
        dataSet.setDrawIcons(false)

        val data = BarData(mutableListOf<IBarDataSet>(dataSet))
        data.setValueTextSize(10f)

        data.setValueTypeface(Typeface.DEFAULT)
        data.barWidth = 0.8f
        barChart.data = data
        barChart.invalidate()

//        barChart.data.notifyDataChanged()
//        barChart.notifyDataSetChanged()

    }

    private fun initRecyclerView() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = TrackItemStatsAdapter(currentTrackItems)
        rvTrackItemStats.adapter = viewAdapter
        rvTrackItemStats.layoutManager = viewManager
    }

    private fun getTrackItemsGroupedByTimeUnit(): Map<String, MutableList<TrackItem>> {

        val map = HashMap<String, MutableList<TrackItem>>()
        currentTrackItems.forEach {
            val key = if (selectedTimeUnit == 0) {
                DateUtils.getWeekYearString(it.date)
            } else {
                DateUtils.getMonthYearString(it.date)
            }

            if (!map.containsKey(key)) {
                map[key] = mutableListOf()
            }
            map[key]!!.add(it)

        }
        return map
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

