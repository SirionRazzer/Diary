package com.sirionrazzer.diary.creator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.sirionrazzer.diary.models.TrackItemTemplateDao
import io.realm.Realm
import java.util.UUID

class TemplateItemCreatorViewModel : ViewModel() {

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    var templateDao = TrackItemTemplateDao(realm)
    var template = MutableLiveData<TrackItemTemplate>()
    var hasChanged: MutableLiveData<Boolean> = MutableLiveData()

    init {
        Diary.app.appComponent.inject(this)
        hasChanged.value = false
        createNewTemplate()
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

    private fun createNewTemplate() {
        val position = templateDao.getAllUnarchivedTemplates().size
        template.value = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            selected = true,
            name = "",
            description = "",
            image = R.drawable.aaa_circlecolor_dumptruck,
            hasTextField = false,
            hasNumberField = false,
            hasPictureField = false,
            position = position
        )
    }

    fun saveNewTemplate() {
        hasChanged.value?.let { it ->
            if (it) {
                template.value?.let {
                    templateDao.addTemplate(it)
                }
            }
        }
    }

    fun setImageResource(id: Int) {
        template.value?.image = id
    }
}