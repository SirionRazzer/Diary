package com.sirionrazzer.diary.models

import android.arch.lifecycle.LiveData
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


    fun deleteTemplate(id: String) {
        realm.executeTransactionAsync {
            val result = it.where(TrackItemTemplate::class.java).equalTo("id", id).findFirst()
            result?.deleteFromRealm()
        }
    }


    fun getAllTemplates(): List<TrackItemTemplate> {
        var items = realm.where(TrackItemTemplate::class.java).findAll().sort("position")
        var templates = mutableListOf<TrackItemTemplate>()


        items.let {
            it.forEach {
                templates.add(it)
            }
        }

        return templates
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