package com.sirionrazzer.diary.models

import io.realm.Realm

class TrackItemTemplateDao(val realm: Realm) {

    fun addTemplate(template: TrackItemTemplate) {
        realm.executeTransaction {
            val item = TrackItemTemplate(
                id = template.id,
                archived = template.archived,
                selected = template.selected,
                name = template.name,
                description = template.description,
                image = template.image,
                hasTextField = template.hasTextField,
                hasNumberField = template.hasNumberField,
                hasPictureField = template.hasPictureField,
                position = template.position
            )
            it.insert(item)
        }
    }

    fun getTemplate(id: String): TrackItemTemplate? {
        return realm.where(TrackItemTemplate::class.java).equalTo("id", id).findFirst()
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
        val items = realm.where(TrackItemTemplate::class.java).findAll().sort("position")
        realm.commitTransaction()
        val list = items.map { item -> item as TrackItemTemplate }
        return list.toMutableList()
    }

    fun getAllUnarchivedTemplates(): MutableList<TrackItemTemplate> {
        realm.beginTransaction()
        val items =
            realm.where(TrackItemTemplate::class.java).equalTo("archived", false).sort("position")
                .findAll()
        realm.commitTransaction()
        val list = items.map { item -> item as TrackItemTemplate }
        return list.toMutableList()
    }

    fun getAllArchivedTemplates(): MutableList<TrackItemTemplate> {
        realm.beginTransaction()
        val items =
            realm.where(TrackItemTemplate::class.java).equalTo("archived", true).sort("position")
                .findAll()
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