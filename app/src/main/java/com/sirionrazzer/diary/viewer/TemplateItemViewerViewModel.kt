package com.sirionrazzer.diary.viewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.sirionrazzer.diary.models.TrackItemTemplateDao
import io.realm.Realm

class TemplateItemViewerViewModel : ViewModel() {

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    var currentTemplateItems: MutableLiveData<MutableList<TrackItemTemplate>> = MutableLiveData()
    private var templateDao = TrackItemTemplateDao(realm)
    var hasChanged: Boolean = false

    init {
        Diary.app.appComponent.inject(this)

        currentTemplateItems.value = getUndeletedTemplates()
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

    private fun getUndeletedTemplates(): MutableList<TrackItemTemplate> {
        return templateDao.getAllUndeletedTemplates()
    }

    private fun getDeletedTemplates(): MutableList<TrackItemTemplate> {
        return templateDao.getAllDeletedTemplates()
    }

    fun updateTemplate(template: TrackItemTemplate) {
        templateDao.updateTemplate(template)
    }

    fun refreshTemplateList() {
        currentTemplateItems.value?.clear()
        currentTemplateItems.value = getUndeletedTemplates()
    }

    fun decreasePositions(start: Int) {
        if (currentTemplateItems.value != null && start < currentTemplateItems.value!!.size) {
            var pos = start
            while (pos < currentTemplateItems.value!!.size) {
                val item = currentTemplateItems.value!!.get(pos)

                val template = TrackItemTemplate()
                template.id = item.id
                template.name = item.name
                template.image = item.image
                template.position = (item.position.dec()) // <-- this
                template.hasTextField = item.hasTextField
                template.hasNumberField = item.hasNumberField
                template.deleted = item.deleted

                updateTemplate(template)

                pos += 1
            }
        }
    }

    /**
     * Deleted template has deleted == true and negative position
     */
    fun deleteTemplate(item: TrackItemTemplate) {
        val template = TrackItemTemplate()
        template.id = item.id
        template.name = item.name
        template.image = item.image
        template.position = (-1) * getDeletedTemplates().size // <- negative position
        template.hasTextField = item.hasTextField
        template.hasNumberField = item.hasNumberField
        template.deleted = true // <-- this

        decreasePositions(item.position + 1)
        updateTemplate(template)
        refreshTemplateList()
        hasChanged = true
    }
}