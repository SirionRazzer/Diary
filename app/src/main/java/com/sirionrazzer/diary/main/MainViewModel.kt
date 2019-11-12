package com.sirionrazzer.diary.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.*
import com.sirionrazzer.diary.util.DateUtils
import io.realm.Realm
import java.util.*
import javax.inject.Inject

@SuppressLint("CheckResult")
class MainViewModel : ViewModel() {

    @Inject
    lateinit var userStorage: UserStorage

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    var currentTrackItems: MutableList<TrackItem> = mutableListOf()
    var deletedTrackItems: MutableList<TrackItem> = mutableListOf()
    var currentTemplateItems: MutableList<TrackItemTemplate> = mutableListOf()
    var deletedTemplateItems: MutableList<TrackItemTemplate> = mutableListOf()

    var date: Long?
        get() = DateUtils.persistDate(Calendar.getInstance().time)
        set(value) {
            date = value
        }

    init {
        initTrackAndTemplateItems()

        Diary.app.appComponent.inject(this)
    }


    fun saveTrackItems() {
        Log.d(
            "MainViewModel",
            "Current count of track items in database is " + realm.trackItemsDao.getCountOfTrackItems()
        )

        currentTrackItems.forEach {
            it.date = date!!
            realm.trackItemsDao.addTrackItem(it)
        }

        deletedTrackItems.forEach {
            it.date = date!!
            realm.trackItemsDao.addTrackItem(it)
        }

        Log.d(
            "MainViewModel",
            "Current count of track items in database is " + realm.trackItemsDao.getCountOfTrackItems()
        )
    }


    fun getTrackItemsFromTemplates() {
        realm.trackItemsTemplatesDao.getAllTemplates()
    }


    fun deleteAllTrackItems() {
        realm.trackItemsDao.deleteAllTrackItems()
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }


    fun createDefaultTrackItems() {
        val tit1 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "work",
            R.drawable.z_circlecolor_workspace,
            hasTextField = false,
            hasNumberField = false,
            position = 10
        )
        val tit2 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "relax",
            R.drawable.z_circlecolor_river,
            hasTextField = false,
            hasNumberField = false,
            position = 9
        )
        val tit3 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "friends",
            R.drawable.z_circlecolor_beach,
            hasTextField = false,
            hasNumberField = false,
            position = 8
        )
        val tit4 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "coding",
            R.drawable.z_circlecolor_abacus,
            hasTextField = false,
            hasNumberField = false,
            position = 7
        )
        val tit5 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "workout",
            R.drawable.z_circlecolor_alarmclock,
            hasTextField = true,
            hasNumberField = false,
            position = 6
        )
        val tit6 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "movie",
            R.drawable.z_circlecolor_photocamera,
            hasTextField = true,
            hasNumberField = false,
            position = 5
        )
        val tit7 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "book",
            R.drawable.z_circlecolor_library,
            hasTextField = true,
            hasNumberField = false,
            position = 4
        )
        val tit8 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "calories",
            R.drawable.z_circlecolor_waste,
            hasTextField = false,
            hasNumberField = true,
            position = 3
        )
        val tit9 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "water",
            R.drawable.z_circlecolor_drop,
            hasTextField = false,
            hasNumberField = true,
            position = 2
        )
        val tit10 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "income",
            R.drawable.z_circlecolor_cash,
            hasTextField = false,
            hasNumberField = true,
            position = 1
        )
        val tit11 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "love",
            R.drawable.z_circlecolor_forest_2,
            hasTextField = true,
            hasNumberField = false,
            position = 0
        )

        realm.trackItemsTemplatesDao.addTemplate(tit1)
        realm.trackItemsTemplatesDao.addTemplate(tit2)
        realm.trackItemsTemplatesDao.addTemplate(tit3)
        realm.trackItemsTemplatesDao.addTemplate(tit4)
        realm.trackItemsTemplatesDao.addTemplate(tit5)
        realm.trackItemsTemplatesDao.addTemplate(tit6)
        realm.trackItemsTemplatesDao.addTemplate(tit7)
        realm.trackItemsTemplatesDao.addTemplate(tit8)
        realm.trackItemsTemplatesDao.addTemplate(tit9)
        realm.trackItemsTemplatesDao.addTemplate(tit10)
        realm.trackItemsTemplatesDao.addTemplate(tit11)

        initTrackAndTemplateItems()
    }


    fun initTrackAndTemplateItems() {
        currentTrackItems.clear()
        deletedTrackItems.clear()
        currentTemplateItems.clear()
        deletedTemplateItems.clear()

        realm.trackItemsTemplatesDao.getAllTemplates().let {
            it.forEach { it ->
                val trackItem = TrackItem(
                    id = UUID.randomUUID().toString(),
                    deleted = it.deleted,
                    name = it.name,
                    imageOn = it.imageOn,
                    hasTextField = it.hasTextField,
                    hasNumberField = it.hasNumberField,
                    status = false,
                    textField = "",
                    numberField = 0f,
                    date = 0,
                    position = it.position
                )
                if (!it.deleted) {
                    currentTrackItems.add(trackItem)
                } else {
                    deletedTrackItems.add(trackItem)
                }
            }

            it.forEach { it ->
                val templateItem = TrackItemTemplate(
                    id = it.id,
                    deleted = it.deleted,
                    name = it.name,
                    imageOn = it.imageOn,
                    hasTextField = it.hasTextField,
                    hasNumberField = it.hasNumberField,
                    position = it.position
                )
                if (!it.deleted) {
                    currentTemplateItems.add(templateItem)
                } else {
                    deletedTemplateItems.add(templateItem)
                }
            }
        }
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