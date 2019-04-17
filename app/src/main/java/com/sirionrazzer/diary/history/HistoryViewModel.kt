package com.sirionrazzer.diary.history

import android.arch.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.models.TrackItemDao
import com.sirionrazzer.diary.util.DateUtils
import io.realm.Realm

class HistoryViewModel: ViewModel() {

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }
    private val trackItemDao: TrackItemDao = realm.trackItemDao
    private val dateUtils = DateUtils()

    var dates: ArrayList<String> = arrayListOf()
    var trackItemsByDate: HashMap<String, ArrayList<TrackItem>> = hashMapOf()

    init {
        var trackItems: List<TrackItem>? = trackItemDao.getAllTrackItemsSortByDate().value
//        var trackItems: List<TrackItem>? = trackItemDao.getAllTrackItemsSortByDate()
        if (trackItems == null) {
            trackItems = arrayListOf()
        }
        var date: String
        trackItems.forEach {
            date = dateUtils.smartDate(dateUtils.dateFromMillis(it.date), false)
            if (!trackItemsByDate.containsKey(date)) {
                trackItemsByDate[date] = arrayListOf()
                dates.add(date)
            }
            trackItemsByDate[date]!!.add(it)
        }

        Diary.app.appComponent.inject(this)
    }
}

private val Realm.trackItemDao: TrackItemDao
    get() {
        return TrackItemDao(this)
    }
