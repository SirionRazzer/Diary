package com.sirionrazzer.diary.main

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.*
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

    init {
        realm.trackItemsTemplatesDao.getAllTrackItemTemplates().let{
            it.forEach{ it ->
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
                    date = 0
                )
                currentTrackItems.add(trackItem)
            }

            it.forEach{it->
                val templateItem = TrackItemTemplate(
                    id = it.id,
                    deleted = it.deleted,
                    name = it.name,
                    imageOn = it.imageOn,
                    imageOff = it.imageOff,
                    hasTextField = it.hasTextField,
                    hasNumberField = it.hasNumberField
                )
                currentTemplateItems.add(templateItem)
            }
        }

        Diary.app.appComponent.inject(this)
    }


    fun saveTrackItems() {
        currentTrackItems.forEach {
            realm.trackItemsDao.addTrackItem(it)
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
            R.drawable.diary_logo,
            R.drawable.diary_logo,
            hasTextField = false,
            hasNumberField = false
        )
        val tit2 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "relax",
            R.drawable.diary_logo,
            R.drawable.diary_logo,
            hasTextField = false,
            hasNumberField = false
        )
        val tit3 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "friends",
            R.drawable.diary_logo,
            R.drawable.diary_logo,
            hasTextField = false,
            hasNumberField = false
        )

        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit1)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit2)
        realm.trackItemsTemplatesDao.addTrackItemTemplate(tit3)
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