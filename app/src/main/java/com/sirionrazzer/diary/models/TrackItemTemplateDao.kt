package com.sirionrazzer.diary.models

import android.arch.lifecycle.LiveData
import io.realm.Realm
import io.realm.RealmResults

class TrackItemTemplateDao(val realm: Realm) {

    fun addTrackItemTemplate(trackItemTemplate: TrackItemTemplate) {
        realm.executeTransactionAsync {
            val item = TrackItemTemplate(
                id = trackItemTemplate.id,
                deleted = trackItemTemplate.deleted,
                name = trackItemTemplate.name,
                imageOn = trackItemTemplate.imageOn,
                imageOff = trackItemTemplate.imageOff,
                hasTextField = trackItemTemplate.hasTextField,
                hasNumberField = trackItemTemplate.hasNumberField
            )
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


    fun getAllTrackItemTemplates(): List<TrackItemTemplate> {
        var items = realm.where(TrackItemTemplate::class.java).findAll()
        var trackItemTemplates = mutableListOf<TrackItemTemplate>()

        items.let {
            it.forEach {
                trackItemTemplates.add(it)
            }
        }

        return trackItemTemplates
    }


    fun deleteAllTrackItemTemplates() {
        realm.executeTransactionAsync {
            val result = it.where(TrackItem::class.java).findAll()
            result.deleteAllFromRealm()
        }
    }

}