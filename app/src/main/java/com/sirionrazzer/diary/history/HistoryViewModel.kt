package com.sirionrazzer.diary.history

import android.util.Log
import androidx.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.models.TrackItemDao
import com.sirionrazzer.diary.util.DateUtils
import io.realm.Realm

class HistoryViewModel : ViewModel() {


    private var _realm: Realm = Realm.getDefaultInstance()


    val realm: Realm
        get() {
            if (_realm.isClosed) {
                _realm = Realm.getDefaultInstance()
            }
            return _realm

        }


    var dates: ArrayList<String> = arrayListOf()
    var trackItemsByDate: HashMap<String, ArrayList<TrackItem>> = hashMapOf()

    init {
        Diary.app.appComponent.inject(this)
    }

    fun loadData() {
        dates = arrayListOf()
        trackItemsByDate = hashMapOf()

        var trackItems: List<TrackItem>? = realm.trackItemDao.getAllTrackItemsSortByDate()
        if (trackItems == null) {
            trackItems = arrayListOf()
        }

        trackItems.forEach {
            val date = DateUtils.smartDate(DateUtils.dateFromMillis(it.date), false)

            if (!trackItemsByDate.containsKey(date)) {
                trackItemsByDate[date] = arrayListOf()
                dates.add(date)
            }

            trackItemsByDate[date]!!.add(it)
        }

        Log.d("HistoryViewModel", trackItems.size.toString())
    }

    override fun onCleared() {
        super.onCleared()
        _realm.close()
    }
}

private val Realm.trackItemDao: TrackItemDao
    get() {
        return TrackItemDao(this)
    }
