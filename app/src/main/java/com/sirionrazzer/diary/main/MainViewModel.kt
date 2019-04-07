package com.sirionrazzer.diary.main

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
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
    var currentTemplateItems: MutableList<TrackItemTemplate> = mutableListOf()

    val dateUtils = DateUtils()
    var date: Long?
        get() = dateUtils.persistDate(Calendar.getInstance().time)
        set(value) {
            date = value
        }

    init {
        initTrackAndTemplateItems()

        Diary.app.appComponent.inject(this)
    }


    fun saveTrackItems() {
        currentTrackItems.forEach {
            if (it.status) {
                realm.trackItemsDao.addTrackItem(it)
            }
        }
    }


    fun getTrackItemsFromTemplates() {
        realm.trackItemsTemplatesDao.getAllTrackItemTemplates()
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
            R.drawable.ic_check,
            R.drawable.ic_uncheck,
            hasTextField = false,
            hasNumberField = false,
            position = 10
        )
        val tit2 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "relax",
            R.drawable.ic_check,
            R.drawable.ic_uncheck,
            hasTextField = false,
            hasNumberField = false,
            position = 9
        )
        val tit3 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "friends",
            R.drawable.ic_check,
            R.drawable.ic_uncheck,
            hasTextField = false,
            hasNumberField = false,
            position = 8
        )
        val tit4 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "coding",
            R.drawable.ic_check,
            R.drawable.ic_uncheck,
            hasTextField = false,
            hasNumberField = false,
            position = 7
        )
        val tit5 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "workout",
            R.drawable.ic_check,
            R.drawable.ic_uncheck,
            hasTextField = true,
            hasNumberField = false,
            position = 6
        )
        val tit6 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "movie",
            R.drawable.ic_check,
            R.drawable.ic_uncheck,
            hasTextField = true,
            hasNumberField = false,
            position = 5
        )
        val tit7 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "book",
            R.drawable.ic_check,
            R.drawable.ic_uncheck,
            hasTextField = true,
            hasNumberField = false,
            position = 4
        )
        val tit8 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "calories",
            R.drawable.ic_check,
            R.drawable.ic_uncheck,
            hasTextField = false,
            hasNumberField = true,
            position = 3
        )
        val tit9 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "water",
            R.drawable.ic_check,
            R.drawable.ic_uncheck,
            hasTextField = false,
            hasNumberField = true,
            position = 2
        )
        val tit10 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "income",
            R.drawable.ic_check,
            R.drawable.ic_uncheck,
            hasTextField = false,
            hasNumberField = true,
            position = 1
        )
        val tit11 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "love",
            R.drawable.ic_check,
            R.drawable.ic_uncheck,
            hasTextField = true,
            hasNumberField = false,
            position = 0
        )

        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit1)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit2)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit3)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit4)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit5)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit6)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit7)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit8)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit9)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit10)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit11)

        initTrackAndTemplateItems()
    }


    private fun initTrackAndTemplateItems() {
        realm.trackItemsTemplatesDao.getAllTrackItemTemplates().let {
            it.forEach { it ->
                val trackItem = TrackItem(
                    id = UUID.randomUUID().toString(),
                    deleted = it.deleted,
                    name = it.name,
                    imageOn = it.imageOn,
                    imageOff = it.imageOff,
                    hasTextField = it.hasTextField,
                    hasNumberField = it.hasNumberField,
                    status = false,
                    textField = "",
                    numberField = 0f,
                    date = 0,
                    position = it.position
                )
                currentTrackItems.add(trackItem)
            }

            it.forEach { it ->
                val templateItem = TrackItemTemplate(
                    id = it.id,
                    deleted = it.deleted,
                    name = it.name,
                    imageOn = it.imageOn,
                    imageOff = it.imageOff,
                    hasTextField = it.hasTextField,
                    hasNumberField = it.hasNumberField,
                    position = it.position
                )
                currentTemplateItems.add(templateItem)
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