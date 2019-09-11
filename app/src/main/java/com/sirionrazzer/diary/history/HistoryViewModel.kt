package com.sirionrazzer.diary.history

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.*
import com.sirionrazzer.diary.util.DateUtils
import io.realm.Realm
import java.nio.charset.Charset
import javax.inject.Inject

class HistoryViewModel : ViewModel() {

    private var _realm: Realm = Realm.getDefaultInstance()

    @Inject
    lateinit var userStorage: UserStorage

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

    fun getJsonData(): ByteArray {
        val items = realm.trackItemDao.getAllTrackItemsSortByDate()
            .map { realm.copyFromRealm(it) }
        val templates = realm.trackItemTemplateDao.getAllTemplates()
            .map { realm.copyFromRealm(it) }

        val model = AppDataModel(items, templates)
        return Gson().toJson(model).toByteArray(Charset.defaultCharset())
    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }

    fun reloadDataFromBytes(bytes: ByteArray?) {
        val json = bytes?.toString(Charset.defaultCharset())
        val hm = Gson().fromJson(json, AppDataModel::class.java)
        realm.trackItemTemplateDao.deleteAllTemplates()
        hm.trackItemTemplates.forEach {
            if (realm.where(TrackItemTemplate::class.java).equalTo(
                    "id",
                    it.id
                ).findFirst() == null
            ) {
                realm.trackItemTemplateDao.addTemplate(it)
            }
        }
        realm.trackItemDao.deleteAllTrackItems()
        hm.trackItems.forEach { realm.trackItemDao.addTrackItem(it) }
    }
}

class AppDataModel(
    val trackItems: List<TrackItem>,
    var trackItemTemplates: List<TrackItemTemplate>
)

private val Realm.trackItemDao: TrackItemDao
    get() {
        return TrackItemDao(this)
    }

private val Realm.trackItemTemplateDao: TrackItemTemplateDao
    get() {
        return TrackItemTemplateDao(this)
    }
