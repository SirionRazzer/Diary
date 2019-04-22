package com.sirionrazzer.diary.trackitem

import androidx.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.sirionrazzer.diary.models.TrackItemTemplateDao
import io.realm.Realm

class TemplateItemViewerViewModel: ViewModel() {

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    var currentTemplateItems: MutableList<TrackItemTemplate> = mutableListOf()
    private var templateDao = TrackItemTemplateDao(realm)
    var hasChanged: Boolean = false


    init {
        Diary.app.appComponent.inject(this)

        currentTemplateItems = getTemplates()
    }


    override fun onCleared() {
        realm.close()
        super.onCleared()
    }


    private fun getTemplates(): MutableList<TrackItemTemplate> {
        return templateDao.getAllTemplates().toMutableList()
    }


    fun updateTemplate(template: TrackItemTemplate) {
        templateDao.updateTemplate(template)
    }


    fun refreshTemplateList() {
        currentTemplateItems.clear()
        currentTemplateItems = getTemplates()
    }
}