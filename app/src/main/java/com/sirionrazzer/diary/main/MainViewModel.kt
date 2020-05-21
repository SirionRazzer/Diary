package com.sirionrazzer.diary.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.models.TrackItemDao
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.sirionrazzer.diary.models.TrackItemTemplateDao
import com.sirionrazzer.diary.models.UserStorage
import com.sirionrazzer.diary.util.DateUtils.Factory.DAY_MILLISECONDS
import io.realm.Realm
import org.threeten.bp.LocalDate
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.ArrayList

@SuppressLint("CheckResult")
class MainViewModel : ViewModel() {

    @Inject
    lateinit var userStorage: UserStorage

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    var currentTrackItems: MutableLiveData<MutableList<TrackItem>> = MutableLiveData()
    var currentTemplateItems: MutableLiveData<MutableList<TrackItemTemplate>> = MutableLiveData()
    var deletedTrackItems: MutableLiveData<MutableList<TrackItem>> = MutableLiveData()
    var deletedTemplateItems: MutableLiveData<MutableList<TrackItemTemplate>> = MutableLiveData()

    var editedIds: ArrayList<String>? = null

    var date: MutableLiveData<LocalDate> = MutableLiveData()

    fun setForDate(date: LocalDate) {
        this.date.value = date
        val dateTrackItems = realm.trackItemsDao.getTrackItemsByDate(this.date.value!!)
        editedIds?.clear()
        if (editedIds == null) editedIds = ArrayList()
        dateTrackItems.forEach { editedIds?.add(it.id) }
        setupTrackAndTemplateItems()
    }

    init {
        Diary.app.appComponent.inject(this)

        currentTrackItems.value = mutableListOf()
        deletedTrackItems.value = mutableListOf()
        currentTemplateItems.value = mutableListOf()
        deletedTemplateItems.value = mutableListOf()

        date.value = LocalDate.now()
    }

    fun saveTrackItems() {
        Log.d(
            "MainViewModel",
            "Current count of track items in database is " + realm.trackItemsDao.getCountOfTrackItems()
        )

        currentTrackItems.value?.forEach {
            it.date = date.value!!.toEpochDay() * DAY_MILLISECONDS
            realm.trackItemsDao.addTrackItem(it)
        }

        deletedTrackItems.value?.forEach {
            it.date = date.value!!.toEpochDay() * DAY_MILLISECONDS
            realm.trackItemsDao.addTrackItem(it)
        }

        Log.d(
            "MainViewModel",
            "Current count of track items in database is " + realm.trackItemsDao.getCountOfTrackItems()
        )
    }

    fun deleteAllTrackItems() {
        realm.trackItemsDao.deleteAllTrackItems()
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

//    fun createDefaultTrackItems() {
//        val tit0 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "Note",
//            R.drawable.aaa_circlecolor_writing,
//            hasTextField = true,
//            hasNumberField = false,
//            position = 0
//        )
//        val tit1 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "Outcome",
//            R.drawable.aaa_circlecolor_credit_card,
//            hasTextField = false,
//            hasNumberField = true,
//            position = 1
//        )
//        val tit2 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "Workout",
//            R.drawable.aab_dumbbell,
//            hasTextField = true,
//            hasNumberField = false,
//            position = 2
//        )
//        val tit3 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "Calories",
//            R.drawable.aab_nofood,
//            hasTextField = false,
//            hasNumberField = true,
//            position = 3
//        )
//        val tit4 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "Water",
//            R.drawable.aab_bottle,
//            hasTextField = false,
//            hasNumberField = true,
//            position = 4
//        )
//        val tit5 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "Footsteps",
//            R.drawable.aab_stopclock,
//            hasTextField = false,
//            hasNumberField = true,
//            position = 5
//        )
//        val tit6 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "Weight",
//            R.drawable.aab_weight,
//            hasTextField = false,
//            hasNumberField = true,
//            position = 6
//        )
//        realm.trackItemsTemplatesDao.addTemplate(tit0)
//        realm.trackItemsTemplatesDao.addTemplate(tit1)
//        realm.trackItemsTemplatesDao.addTemplate(tit2)
//        realm.trackItemsTemplatesDao.addTemplate(tit3)
//        realm.trackItemsTemplatesDao.addTemplate(tit4)
//        realm.trackItemsTemplatesDao.addTemplate(tit5)
//        realm.trackItemsTemplatesDao.addTemplate(tit6)
//    }

    private fun initTrackAndTemplateItems() {
        clearAllLists()

        realm.trackItemsTemplatesDao.getAllUnarchivedTemplates().let {
            it.forEach { tit ->
                addEmptyItemToCurrent(tit)
            }
        }
    }

    private fun addEmptyItemToCurrent(it: TrackItemTemplate) {
        val trackItem = TrackItem(
            id = UUID.randomUUID().toString(),
            templateId = it.id,
            archived = it.archived,
            name = it.name,
            description = it.description,
            image = it.image,
            hasTextField = it.hasTextField,
            hasNumberField = it.hasNumberField,
            hasPictureField = it.hasPictureField,
            status = false,
            textField = "",
            numberField = 0f,
            pictureField = "",
            date = date.value!!.toEpochDay() * DAY_MILLISECONDS, // TODO
            position = it.position
        )
        currentTrackItems.value?.add(trackItem)

        val templateItem = TrackItemTemplate(
            id = it.id,
            archived = it.archived,
            selected = it.selected,
            name = it.name,
            description = it.description,
            image = it.image,
            hasTextField = it.hasTextField,
            hasNumberField = it.hasNumberField,
            hasPictureField = it.hasPictureField,
            position = it.position
        )
        currentTemplateItems.value?.add(templateItem)
    }

    fun setupTrackAndTemplateItems() {
        clearAllLists()
        initTrackAndTemplateItems()

        if (editedIds != null) {
            for (e in (editedIds as ArrayList)) {
                realm.trackItemsDao.getTrackItemById(e)?.let {
                    val trackItem = TrackItem(
                        id = it.id,
                        templateId = it.templateId,
                        archived = it.archived,
                        name = it.name,
                        description = it.description,
                        image = it.image,
                        hasTextField = it.hasTextField,
                        hasNumberField = it.hasNumberField,
                        hasPictureField = it.hasPictureField,
                        status = it.status,
                        textField = it.textField,
                        numberField = it.numberField,
                        pictureField = it.pictureField,
                        date = date.value!!.toEpochDay() * DAY_MILLISECONDS,
                        position = it.position
                    )
                    mergeToCurrent(trackItem)
                }
            }
        }
    }

    private fun mergeToCurrent(it: TrackItem) {
        // item can be: present + filled/unfilled, deleted + filled/unfilled
        // replace or append
        var present = false

        // try to replace
        if (currentTrackItems.value != null && currentTemplateItems.value != null) {
            for (e in currentTrackItems.value!!) {
                if (e.templateId.equals(it.templateId)) {
                    present = true
                    e.id = it.id
                    e.archived = it.archived
                    e.hasTextField = it.hasTextField
                    e.hasNumberField = it.hasNumberField
                    e.status = it.status
                    e.textField = it.textField
                    e.numberField = it.numberField
                    e.date = it.date
                    break
                }
            }

            if (!present) {
                deletedTrackItems.value?.add(it)
                realm.trackItemsTemplatesDao.getTemplate(it.templateId)?.let {
                    deletedTemplateItems.value?.add(it)
                }
            }

            sort()
        }
    }

    fun sort() {
        currentTrackItems.value?.sortBy { it.position }
        currentTemplateItems.value?.sortBy { it.position }
        deletedTrackItems.value?.sortBy { it.position }
        deletedTemplateItems.value?.sortBy { it.position }
    }

    fun clearAllLists() {
        currentTrackItems.value?.clear()
        deletedTrackItems.value?.clear()
        currentTemplateItems.value?.clear()
        deletedTemplateItems.value?.clear()
    }
}

private val Realm.trackItemsDao: TrackItemDao
    get() {
        return TrackItemDao(this)
    }

private val Realm.trackItemsTemplatesDao: TrackItemTemplateDao
    get() {
        return TrackItemTemplateDao(this)
    }