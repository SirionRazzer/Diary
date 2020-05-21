package com.sirionrazzer.diary.boarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.sirionrazzer.diary.models.TrackItemTemplateDao
import io.realm.Realm
import java.util.UUID

class BoardingPickerViewModel(app: Application) : AndroidViewModel(app) {

    private var _realm: Realm = Realm.getDefaultInstance()

    lateinit var data: LiveData<List<TrackItemTemplate>>

    val realm: Realm
        get() {
            if (_realm.isClosed) {
                _realm = Realm.getDefaultInstance()
            }
            return _realm
        }

    fun createTrackItems() {
        var counter = 0
        data.value?.indices?.forEach {
            if (data.value!!.get(it).selected) {
                val tit = TrackItemTemplate(
                    UUID.randomUUID().toString(),
                    false,
                    selected = data.value!!.get(it).selected,
                    name = data.value!!.get(it).name,
                    description = data.value!!.get(it).description,
                    image = data.value!!.get(it).image,
                    hasTextField = data.value!!.get(it).hasTextField,
                    hasNumberField = data.value!!.get(it).hasNumberField,
                    hasPictureField = data.value!!.get(it).hasPictureField,
                    position = counter
                )
                realm.trackItemsTemplatesDao.addTemplate(tit)
                counter += 1
            }
        }
    }

    // HACK to close all realms in case user reencrypts Realm (creates account, changes password)
    fun closeRealms() {
        realm.close()
        _realm.close()
    }

    fun checkExampleTemplate(position: Int, value: Boolean) {
        data.value?.get(position)?.selected = value
    }
}

private val Realm.trackItemsTemplatesDao: TrackItemTemplateDao
    get() {
        return TrackItemTemplateDao(this)
    }