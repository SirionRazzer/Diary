package com.sirionrazzer.diary.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.models.UserStorage
import com.sirionrazzer.diary.trackitem.TrackItemCreatorActivity
import com.sirionrazzer.diary.trackitem.TrackItemViewerActivity
import com.sirionrazzer.diary.util.DateUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userStorage: UserStorage

    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: TemplatesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Diary.app.appComponent.inject(this)

        mainViewModel = createViewModel()

        toolbar.setTitle(R.string.title_my_day)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        if (userStorage.userSettings.firstTime) {
            mainViewModel.createDefaultTrackItems()
            userStorage.updateSettings {
                it.firstTime = false
            }
        }

        adapter = TemplatesAdapter(this, mainViewModel)
        gwTemplates.adapter = adapter

        btnManageActivities.setOnClickListener{
            startActivity<TrackItemViewerActivity>()
        }
    }


    override fun onResume() {
        super.onResume()
        val dateUtils = DateUtils()
        tvDate.text = dateUtils.smartDate(dateUtils.dateFromMillis(mainViewModel.date), false)
    }


    private fun createNewTrackItem() {
        startActivity<TrackItemCreatorActivity>()
        finish()
    }


    private fun save() {
        mainViewModel.saveTrackItems()
    }


    fun createViewModel(): MainViewModel {
        return ViewModelProviders.of(this).get(MainViewModel::class.java)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_save_log, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mSaveLog) {
            save()
            finish()
        } else {
            onBackPressed()
        }
        return true
    }
}
