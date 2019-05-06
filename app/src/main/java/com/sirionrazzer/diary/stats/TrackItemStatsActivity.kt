package com.sirionrazzer.diary.stats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.MenuItem
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
import java.util.*

class TrackItemStatsActivity : AppCompatActivity() {

    var currentTrackItems: MutableList<TrackItem> = mutableListOf()
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter: TrackItemStatsAdapter
    private lateinit var template: TrackItemTemplate

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

        initTrackItems()

        initTrackAndTemplateItems(trackItemName)
        initRecyclerView()
    }

    private fun initTrackItems() {
        val cal = Calendar.getInstance()
        val trackItem = TrackItem(
            id = UUID.randomUUID().toString(),
            deleted = false,
            name = "work",
            imageOn = R.drawable.ic_check,
            imageOff = R.drawable.ic_uncheck,
            hasTextField = true,
            hasNumberField = true,
            status = true,
            textField = "dnes som stravil v praci tolko casu",
            numberField = 123.323f,
            date = DateUtils().persistDate(cal.time)!!,
            position = 1
        )
        cal.add(Calendar.DATE, -1)
        val trackItem2 = TrackItem(
            id = UUID.randomUUID().toString(),
            deleted = false,
            name = "work",
            imageOn = R.drawable.ic_check,
            imageOff = R.drawable.ic_uncheck,
            hasTextField = true,
            hasNumberField = false,
            status = false,
            textField = "dnes som spravil android appku",
            numberField = 0f,
            date = DateUtils().persistDate(cal.time)!!,
            position = 2
        )
        cal.add(Calendar.DATE, -1)

        val trackItem3 = TrackItem(
            id = UUID.randomUUID().toString(),
            deleted = false,
            name = "work",
            imageOn = R.drawable.ic_check,
            imageOff = R.drawable.ic_uncheck,
            hasTextField = false,
            hasNumberField = true,
            status = true,
            textField = "",
            numberField = 231.24f,
            date = DateUtils().persistDate(cal.time)!!,
            position = 3
        )

        currentTrackItems.add(trackItem)
        currentTrackItems.add(trackItem2)
        currentTrackItems.add(trackItem3)
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

