package com.sirionrazzer.diary.models

import android.arch.lifecycle.LiveData
import java.util.*
import io.realm.Realm
import io.realm.RealmResults

class TrackItemDao(val realm: Realm) {


    fun addTrackItem(trackItem: TrackItem) {
        realm.executeTransactionAsync {
            val item = TrackItem()
            item.id = Calendar.getInstance().timeInMillis.toString() + " " + UUID.randomUUID().toString()
            item.hasNumberField = trackItem.hasNumberField
            item.numberField = trackItem.numberField
            item.hasTextField = trackItem.hasNumberField
            item.textField = trackItem.textField
            item.date = trackItem.date
            it.insert(item)
        }
    }


    fun getTrackItem(id: String): LiveData<RealmResults<TrackItem>> {
        var result = realm.where(TrackItem::class.java).equalTo("id", id).findAllAsync()
        return RealmLiveData<TrackItem>(result)
    }


    fun deleteTrackItem(id: String) {
        realm.executeTransactionAsync {
            val result = it.where(TrackItem::class.java).equalTo("id", id).findFirst()
            result?.deleteFromRealm()
        }
    }


    fun getAllTrackItems(): LiveData<RealmResults<TrackItem>> {
        var items = realm.where(TrackItem::class.java).findAll()
        return RealmLiveData<TrackItem>(items)
    }


    fun deleteAllTrackItems() {
        realm.executeTransactionAsync {
            val result = it.where(TrackItem::class.java).findAll()
            result.deleteAllFromRealm()
        }
    }
}
