package com.sirionrazzer.diary.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.*
import com.sirionrazzer.diary.util.DateUtils
import io.realm.Realm
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
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
    var strikeLength = MutableLiveData<Int>(0)

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

        // compute strikeLength also
        val today = LocalDateTime.now()
        val todayLong: Long = today.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val todayKey = DateUtils.smartDate(DateUtils.dateFromMillis(todayLong), false)
        if (trackItemsByDate.containsKey(todayKey)) {
            strikeLength.value = computeStrikeLength(todayLong, 1)
        } else {
            strikeLength.value = computeStrikeLength(
                today.minusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                0
            )
        }
        Log.d("HistoryViewModel", trackItems.size.toString())
        //realm.close()
    }

    private fun computeStrikeLength(dayMillis: Long, depth: Int): Int {
        val key = DateUtils.smartDate(DateUtils.dateFromMillis(dayMillis), false)
        val previousDay: Long = Instant.ofEpochMilli(dayMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .minusDays(1)
            .toEpochDay() * DateUtils.DAY_MILLISECONDS
        if (trackItemsByDate.containsKey(key)) {
            return 1 + computeStrikeLength(previousDay, depth + 1)
        } else {
            return 0
        }
    }

    fun getJsonData(): ByteArray {
        val items = realm.trackItemDao.getAllTrackItemsSortByDate()
            .map { realm.copyFromRealm(it) }
        val templates = realm.trackItemTemplateDao.getAllTemplates()
            .map { realm.copyFromRealm(it) }

        val model = AppDataModel(items, templates)
        //realm.close()
        return Gson().toJson(model).toByteArray(Charset.defaultCharset())
    }

    // HACK to close all realms in case user reencrypts Realm (creates account, changes password)
    fun closeRealms() {
        realm.close()
        _realm.close()
    }

    override fun onCleared() {
        super.onCleared()
        //realm.close()
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
        //realm.close()
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
