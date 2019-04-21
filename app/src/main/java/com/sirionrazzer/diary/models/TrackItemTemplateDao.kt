package com.sirionrazzer.diary.models

import androidx.lifecycle.LiveData
import io.realm.Realm
import io.realm.RealmResults

class TrackItemTemplateDao(val realm: Realm) {

    fun addTemplate(trackItemTemplate: TrackItemTemplate) {
        realm.executeTransaction {
            val item = TrackItemTemplate(
                id = trackItemTemplate.id,
                deleted = trackItemTemplate.deleted,
                name = trackItemTemplate.name,
                imageOn = trackItemTemplate.imageOn,
                imageOff = trackItemTemplate.imageOff,
                hasTextField = trackItemTemplate.hasTextField,
                hasNumberField = trackItemTemplate.hasNumberField,
                position = trackItemTemplate.position
            )
            it.insert(item)
        }
    }


    fun getTemplate(id: String): LiveData<RealmResults<TrackItemTemplate>> {
        var result = realm.where(TrackItemTemplate::class.java).equalTo("id", id).findAllAsync()
        return RealmLiveData<TrackItemTemplate>(result)
    }

    fun getTemplateByName(name: String): TrackItemTemplate? {
        return realm.where(TrackItemTemplate::class.java).equalTo("name", name).findFirst()
    }


    fun deleteTemplate(id: String) {
        realm.executeTransactionAsync {
            val result = it.where(TrackItemTemplate::class.java).equalTo("id", id).findFirst()
            result?.deleteFromRealm()
        }
    }


    fun getAllTemplates(): MutableList<TrackItemTemplate> {
        realm.beginTransaction()
        var items = realm.where(TrackItemTemplate::class.java).findAll().sort("position")
        realm.commitTransaction()
        val list = items.map { item -> item as TrackItemTemplate }
        return list.toMutableList()
    }


    fun deleteAllTemplates() {
        realm.executeTransactionAsync {
            val result = it.where(TrackItem::class.java).findAll()
            result.deleteAllFromRealm()
        }
    }


    fun updateTemplate(updatedTemplate: TrackItemTemplate) {
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(updatedTemplate)
        realm.commitTransaction()
    }

}