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
        return templateDao.getAllUnarchivedTemplates()
    }

    private fun getDeletedTemplates(): MutableList<TrackItemTemplate> {
        return templateDao.getAllArchivedTemplates()
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

                val template = TrackItemTemplate().apply {
                    id = item.id
                    name = item.name
                    image = item.image
                    position = (item.position.dec()) // <-- this
                    hasTextField = item.hasTextField
                    hasNumberField = item.hasNumberField
                    archived = item.archived
                    description = item.description
                    hasPictureField = item.hasPictureField
                    selected = item.selected
                }

                updateTemplate(template)

                pos += 1
            }
        }
    }

    /**
     * Deleted template has deleted == true and negative position
     */
    fun deleteTemplate(item: TrackItemTemplate) {
        val template = TrackItemTemplate().apply {
            id = item.id
            name = item.name
            image = item.image
            position = (-1) * getDeletedTemplates().size // <- negative position
            hasTextField = item.hasTextField
            hasNumberField = item.hasNumberField
            archived = true // <-- this
            description = item.description
            hasPictureField = item.hasPictureField
            selected = item.selected
        }

        decreasePositions(item.position + 1)
        updateTemplate(template)
        refreshTemplateList()
        hasChanged = true
    }
}