package com.sirionrazzer.diary.stats

import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
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
    private val milisecondsInWeek = 604800000f

    private var barMap: HashMap<Long, Float> = HashMap()

    private lateinit var itemsGrouped: Map<String, MutableList<TrackItem>>

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

        initTrackAndTemplateItems(trackItemName)

        initRecyclerView()
    }

    private fun initBarChart() {
        barChart.setDrawGridBackground(false)

        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
//        barChart.xAxis.axisMinimum = barMap.keys.min()!!.toFloat()-604800f
//        barChart.xAxis.axisMaximum = barMap.keys.max()!!.toFloat()+604800f
        barChart.xAxis.setDrawGridLines(false)
//        barChart.xAxis.labelCount = 7
        barChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {

                val pair = DateUtils.getWeekAndYearPair(value.toLong())
                return "${pair.first}-${pair.second}"
            }
        }


        barChart.axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisLeft.spaceTop = 15f
//        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.valueFormatter = CustomFormatter(barChart)

        barChart.axisRight.isEnabled = false

        setData()
    }

    private fun getTrackItemsGroupedByTimeUnit(): Map<String, MutableList<TrackItem>> {

        val map = HashMap<String, MutableList<TrackItem>>()
        currentTrackItems.forEach {

            val pair = DateUtils.getWeekAndYearPair(it.date)
            val key = "${pair.first}-${pair.second}"
            if (!map.containsKey(key)) {
                map[key] = mutableListOf()
            }
            map[key]!!.add(it)

        }
        return map
    }

    private fun setData() {
        val values = mutableListOf<BarEntry>()

//        values.add(BarEntry(1000f, 20f))

        for (i in 1..20) {
            val f = Math.random() * 11

            values.add(BarEntry(i.toFloat(), f.toFloat()))

        }
//        barMap.forEach { (key, value) ->
//            values.add(BarEntry(key.toFloat(), value))
//        }
        val dataSet = BarDataSet(values, template.name)
        dataSet.setDrawIcons(false)

        val data = BarData(mutableListOf<IBarDataSet>(dataSet))
        data.setValueTextSize(10f)

        data.setValueTypeface(Typeface.DEFAULT)
        data.barWidth = 0.8f
        barChart.data = data


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        onBackPressed()
        return true
    }

    private fun initRecyclerView() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = TrackItemStatsAdapter(currentTrackItems)
        rvTrackItemStats.adapter = viewAdapter
        rvTrackItemStats.layoutManager = viewManager
    }


    private fun initTrackAndTemplateItems(name: String) {
        currentTrackItems.addAll(realm.trackItemsDao.getTrackItemsWithName(name))
        if (template.hasNumberField) {
            itemsGrouped = getTrackItemsGroupedByTimeUnit()

            itemsGrouped.forEach { (key, value) ->
                var sum = 0f
                value.forEach {
                    sum += it.numberField!!
                }
                barMap[DateUtils.getFirstDayOfTheWeekInMiliseconds(value[0].date)] = sum / value.count()
            }
            initBarChart()

        } else {
            barChart.visibility = View.GONE
        }

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

