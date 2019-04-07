package com.sirionrazzer.diary.trackitem

import android.arch.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.sirionrazzer.diary.models.TrackItemTemplateDao
import io.realm.Realm

class TrackItemViewerViewModel: ViewModel() {

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    var currentTemplateItems: MutableList<TrackItemTemplate> = mutableListOf()
    var templateDao = TrackItemTemplateDao(realm)

    init {
        Diary.app.appComponent.inject(this)
    }


    override fun onCleared() {
        realm.close()
        super.onCleared()
    }


    fun getTemplates() {
        currentTemplateItems = templateDao.getAllTrackItemTemplates().toMutableList()
    }


    fun updateTemplate(template: TrackItemTemplate) {
        templateDao.updateTrackItemTemplate(template)
    }

}