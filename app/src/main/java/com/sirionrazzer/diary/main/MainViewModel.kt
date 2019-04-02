package com.sirionrazzer.diary.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.*
import io.realm.Realm
import io.realm.RealmResults
import java.util.*
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var userStorage: UserStorage

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }


    init {
        Diary.app.appComponent.inject(this)
    }


    fun loadTrackItems(): LiveData<RealmResults<TrackItem>> {
        return realm.trackItemsDao.getAllTrackItems()
    }


    fun saveTrackItem(trackItem: TrackItem) {
        realm.trackItemsDao.addTrackItem(trackItem)
    }


    override fun onCleared() {
        realm.close()
        super.onCleared()
    }


    fun createDefaultTrackItems() {
        var tit1 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "work",
            "",
            "",
            false,
            false
        )
        var tit2 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "relax",
            "",
            "",
            false,
            false
        )
        var tit3 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "friends",
            "",
            "",
            false,
            false
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