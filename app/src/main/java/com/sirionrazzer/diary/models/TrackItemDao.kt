package com.sirionrazzer.diary.models

import androidx.lifecycle.LiveData
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class TrackItemDao(val realm: Realm) {

    fun addTrackItem(trackItem: TrackItem) {
        realm.executeTransaction {
            //item.id = Calendar.getInstance().timeInMillis.toString() + " " + UUID.randomUUID().toString()
            val item = TrackItem(
                id = trackItem.id,
                templateId = trackItem.templateId,
                deleted = trackItem.deleted,
                name = trackItem.name,
                image = trackItem.image,
                hasTextField = trackItem.hasTextField,
                hasNumberField = trackItem.hasNumberField,
                status = trackItem.status,
                textField = trackItem.textField,
                numberField = trackItem.numberField,
                date = trackItem.date,
                position = trackItem.position
            )
            it.insertOrUpdate(item)
        }
    }

    fun getTrackItemById(id: String): TrackItem? {
        return realm.where(TrackItem::class.java).equalTo("id", id).findFirst()
    }

    fun deleteTrackItem(id: String) {
        realm.executeTransactionAsync {
            val result = it.where(TrackItem::class.java).equalTo("id", id).findFirst()
            result?.deleteFromRealm()
        }
    }

    fun getTrackItemsWithName(name: String): List<TrackItem> {
        val realmItems =
            realm.where(TrackItem::class.java).equalTo("name", name).findAll()
                .sort("date", Sort.DESCENDING)

        return realmItems.map { item -> item as TrackItem }
    }

    fun getCountOfTrackItems(): Long {
        return realm.where(TrackItem::class.java).count()
    }

    fun getAllTrackItems(): LiveData<RealmResults<TrackItem>> {
        var items = realm.where(TrackItem::class.java).findAll()
        items.sort("position")
        return RealmLiveData<TrackItem>(items.sort("position"))
    }

    fun getAllTrackItemsSortByDate(): List<TrackItem> {
        var items = realm.where(TrackItem::class.java).findAll().sort("date")
        return items.map { item -> item as TrackItem }
    }

//    fun getAllTrackItemsSortByDate(): List<TrackItem> {
//
//        val historyItems: ArrayList<TrackItem> = arrayListOf()
//
//        historyItems.add(TrackItem("1", false, "item 1", 1, 1, false, false, true, null, null, 0, 0))
//        historyItems.add(TrackItem("2", false, "item 2", 1, 1, false, false, false, null, null, 0, 0))
//        historyItems.add(TrackItem("3", false, "item 3", 1, 1, false, false, true, null, null, 0, 0))
//        historyItems.add(TrackItem("4", false, "item 4", 1, 1, false, false, true, null, null, 0, 0))
//        historyItems.add(TrackItem("5", false, "item 5", 1, 1, false, false, true, null, null, 0, 0))
//        historyItems.add(TrackItem("6", false, "item 6", 1, 1, false, false, false, null, null, 0, 0))
//        historyItems.add(TrackItem("7", false, "item 7", 1, 1, false, false, true, null, null, 0, 0))
//        historyItems.add(TrackItem("10", false, "item 10", 1, 1, true, false, true, "some text", null, 0, 0))
//        historyItems.add(TrackItem("11", false, "item 11", 1, 1, true, false, false, "some text", null, 0, 0))
//        historyItems.add(TrackItem("12", false, "item 12", 1, 1, false, true, true, null, 42.toFloat(), 0, 0))
//
//        historyItems.add(TrackItem("21", false, "item 21", 1, 1, false, false, true, null, null, 87000000, 0))
//        historyItems.add(TrackItem("22", false, "item 22", 1, 1, false, false, false, null, null, 87000000, 0))
//        historyItems.add(TrackItem("23", false, "item 23", 1, 1, false, false, true, null, null, 87000000, 0))
//
//        historyItems.add(TrackItem("34", false, "item 34", 1, 1, false, false, true, null, null, 173000000, 0))
//        historyItems.add(TrackItem("35", false, "item 35", 1, 1, false, false, true, null, null, 173000000, 0))
//        historyItems.add(TrackItem("36", false, "item 36", 1, 1, false, false, false, null, null, 173000000, 0))
//        historyItems.add(TrackItem("310", false, "item 310", 1, 1, true, false, true, "some text", null, 173000000, 0))
//        historyItems.add(TrackItem("311", false, "item 311", 1, 1, true, false, false, "some text", null, 173000000, 0))
//        historyItems.add(TrackItem("312", false, "item 312", 1, 1, false, true, true, null, 42.toFloat(), 173000000, 0))
//
//        return historyItems
//    }

    fun deleteAllTrackItems() {
        realm.executeTransactionAsync {
            val result = it.where(TrackItem::class.java).findAll()
            result.deleteAllFromRealm()
        }
    }
}
