package com.sirionrazzer.diary.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.models.TrackItemDao
import com.sirionrazzer.diary.models.UserStorage
import io.realm.Realm
import io.realm.RealmResults
import javax.inject.Inject

class MainViewModel: ViewModel() {

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

}


private val Realm.trackItemsDao: TrackItemDao
    get() { return TrackItemDao(this) }