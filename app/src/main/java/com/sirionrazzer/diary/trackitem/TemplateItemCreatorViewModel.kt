package com.sirionrazzer.diary.trackitem

import android.arch.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.sirionrazzer.diary.models.TrackItemTemplateDao
import io.realm.Realm

class TemplateItemCreatorViewModel: ViewModel() {

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }


    var templateDao = TrackItemTemplateDao(realm)
    var hasChanged: Boolean = false

    lateinit var newTemplate: TrackItemTemplate

    init {
        Diary.app.appComponent.inject(this)

        createNewTemplate()
    }


    override fun onCleared() {
        realm.close()
        super.onCleared()
    }


    fun createNewTemplate() {

    }


    fun saveNewTemplate() {

    }

}