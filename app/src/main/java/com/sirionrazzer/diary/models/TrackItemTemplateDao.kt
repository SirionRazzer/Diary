package com.sirionrazzer.diary.models

import android.arch.lifecycle.LiveData
import java.util.*
import io.realm.Realm
import io.realm.RealmResults

class TrackItemTemplateDao(val realm: Realm) {


    fun addTrackItemTemplate(trackItemTemplate: TrackItemTemplate) {
        realm.executeTransactionAsync {
            val item = TrackItemTemplate()
            item.id = Calendar.getInstance().timeInMillis.toString() + " " + UUID.randomUUID().toString()
            item.deleted = trackItemTemplate.deleted
            item.name = trackItemTemplate.name
            item.imageOn = trackItemTemplate.imageOn
            item.imageOff = trackItemTemplate.imageOff
            it.insert(item)
        }
    }


    fun getTrackItemTemplate(id: String): LiveData<RealmResults<TrackItemTemplate>> {
        var result = realm.where(TrackItemTemplate::class.java).equalTo("id", id).findAllAsync()
        return RealmLiveData<TrackItemTemplate>(result)
    }


    fun deleteTrackItemTemplate(id: String) {
        realm.executeTransactionAsync {
            val result = it.where(TrackItemTemplate::class.java).equalTo("id", id).findFirst()
            result?.deleteFromRealm()
        }
    }


    fun getAllTrackItemTemplates(): LiveData<RealmResults<TrackItemTemplate>> {
        var items = realm.where(TrackItemTemplate::class.java).findAll()
        return RealmLiveData<TrackItemTemplate>(items)
    }


    fun deleteAllTrackItemTemplates() {
        realm.executeTransactionAsync {
            val result = it.where(TrackItem::class.java).findAll()
            result.deleteAllFromRealm()
        }
    }

}