package com.sirionrazzer.diary.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.*
import com.sirionrazzer.diary.util.DateUtils.Factory.DAY_MILLISECONDS
import io.realm.Realm
import org.threeten.bp.LocalDate
import java.util.*
import javax.inject.Inject

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

    var date: LocalDate

    init {
        Diary.app.appComponent.inject(this)

        currentTrackItems.value = mutableListOf()
        deletedTrackItems.value = mutableListOf()
        currentTemplateItems.value = mutableListOf()
        deletedTemplateItems.value = mutableListOf()

        date = LocalDate.now()
    }

    fun saveTrackItems() {
        Log.d(
            "MainViewModel",
            "Current count of track items in database is " + realm.trackItemsDao.getCountOfTrackItems()
        )

        currentTrackItems.value?.forEach {
            it.date = date.toEpochDay() * DAY_MILLISECONDS
            realm.trackItemsDao.addTrackItem(it)
        }

        deletedTrackItems.value?.forEach {
            it.date = date.toEpochDay() * DAY_MILLISECONDS
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

    fun createDefaultTrackItems() {
        val tit1 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "work",
            R.drawable.z_circlecolor_workspace,
            hasTextField = false,
            hasNumberField = false,
            position = 4
        )
        val tit2 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "relax",
            R.drawable.z_circlecolor_river,
            hasTextField = false,
            hasNumberField = false,
            position = 3
        )
//        val tit3 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "friends",
//            R.drawable.z_circlecolor_beach,
//            hasTextField = false,
//            hasNumberField = false,
//            position = 8
//        )
//        val tit4 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "coding",
//            R.drawable.z_circlecolor_abacus,
//            hasTextField = false,
//            hasNumberField = false,
//            position = 7
//        )
//        val tit5 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "workout",
//            R.drawable.z_circlecolor_alarmclock,
//            hasTextField = true,
//            hasNumberField = false,
//            position = 6
//        )
//        val tit6 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "movie",
//            R.drawable.z_circlecolor_photocamera,
//            hasTextField = true,
//            hasNumberField = false,
//            position = 5
//        )
//        val tit7 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "book",
//            R.drawable.z_circlecolor_library,
//            hasTextField = true,
//            hasNumberField = false,
//            position = 4
//        )
//        val tit8 = TrackItemTemplate(
//            UUID.randomUUID().toString(),
//            false,
//            "calories",
//            R.drawable.z_circlecolor_waste,
//            hasTextField = false,
//            hasNumberField = true,
//            position = 3
//        )
        val tit9 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "water",
            R.drawable.z_circlecolor_drop,
            hasTextField = false,
            hasNumberField = true,
            position = 2
        )
        val tit10 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "income",
            R.drawable.z_circlecolor_cash,
            hasTextField = false,
            hasNumberField = true,
            position = 1
        )
        val tit11 = TrackItemTemplate(
            UUID.randomUUID().toString(),
            false,
            "love",
            R.drawable.z_circlecolor_forest_2,
            hasTextField = true,
            hasNumberField = false,
            position = 0
        )

        realm.trackItemsTemplatesDao.addTemplate(tit1)
        realm.trackItemsTemplatesDao.addTemplate(tit2)
//        realm.trackItemsTemplatesDao.addTemplate(tit3)
//        realm.trackItemsTemplatesDao.addTemplate(tit4)
//        realm.trackItemsTemplatesDao.addTemplate(tit5)
//        realm.trackItemsTemplatesDao.addTemplate(tit6)
//        realm.trackItemsTemplatesDao.addTemplate(tit7)
//        realm.trackItemsTemplatesDao.addTemplate(tit8)
        realm.trackItemsTemplatesDao.addTemplate(tit9)
        realm.trackItemsTemplatesDao.addTemplate(tit10)
        realm.trackItemsTemplatesDao.addTemplate(tit11)

        initTrackAndTemplateItems()
    }

    private fun initTrackAndTemplateItems() {
        clearAllLists()

        realm.trackItemsTemplatesDao.getAllUndeletedTemplates().let {
            it.forEach { tit ->
                addEmptyItemToCurrent(tit)
            }
        }
    }

    private fun addEmptyItemToCurrent(it: TrackItemTemplate) {
        val trackItem = TrackItem(
            id = UUID.randomUUID().toString(),
            templateId = it.id,
            deleted = it.deleted,
            name = it.name,
            image = it.image,
            hasTextField = it.hasTextField,
            hasNumberField = it.hasNumberField,
            status = false,
            textField = "",
            numberField = 0f,
            date = date.toEpochDay() * DAY_MILLISECONDS,   // TODO
            position = it.position
        )
        currentTrackItems.value?.add(trackItem)

        val templateItem = TrackItemTemplate(
            id = it.id,
            deleted = it.deleted,
            name = it.name,
            image = it.image,
            hasTextField = it.hasTextField,
            hasNumberField = it.hasNumberField,
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
                        deleted = it.deleted,
                        name = it.name,
                        image = it.image,
                        hasTextField = it.hasTextField,
                        hasNumberField = it.hasNumberField,
                        status = it.status,
                        textField = it.textField,
                        numberField = it.numberField,
                        date = date.toEpochDay() * DAY_MILLISECONDS,
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
                    e.deleted = it.deleted
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