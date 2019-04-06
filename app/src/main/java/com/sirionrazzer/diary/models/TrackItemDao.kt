package com.sirionrazzer.diary.models

import android.arch.lifecycle.LiveData
import io.realm.Realm
import io.realm.RealmResults

class TrackItemDao(val realm: Realm) {

    fun addTrackItem(trackItem: TrackItem) {
        realm.executeTransaction {
            //item.id = Calendar.getInstance().timeInMillis.toString() + " " + UUID.randomUUID().toString()
            val item = TrackItem(
                id = trackItem.id,
                deleted = trackItem.deleted,
                name = trackItem.name,
                imageOn = trackItem.imageOn,
                imageOff = trackItem.imageOff,
                hasTextField = trackItem.hasTextField,
                hasNumberField = trackItem.hasNumberField,
                status = trackItem.status,
                textField = trackItem.textField,
                numberField = trackItem.numberField,
                date = trackItem.date
            )
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
