package com.sirionrazzer.diary.creator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.sirionrazzer.diary.models.TrackItemTemplateDao
import io.realm.Realm
import java.util.*

class TemplateItemCreatorViewModel : ViewModel() {

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    var templateDao = TrackItemTemplateDao(realm)
    var template = MutableLiveData<TrackItemTemplate>()
    var hasChanged: Boolean = false

    init {
        Diary.app.appComponent.inject(this)

        createNewTemplate()
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

    private fun createNewTemplate() {
        val position = templateDao.getAllUndeletedTemplates().size
        template.value = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "",
            R.drawable.z_circlecolor_dumptruck,
            hasTextField = false,
            hasNumberField = false,
            position = position
        )
    }

    fun saveNewTemplate() {
        template.value?.let {
            templateDao.addTemplate(it)
        }
    }

    fun setImageResource(id: Int) {
        template.value?.image = id
    }
}