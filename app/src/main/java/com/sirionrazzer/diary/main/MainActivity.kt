package com.sirionrazzer.diary.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.models.UserStorage
import com.sirionrazzer.diary.trackitem.TrackItemCreatorActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
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

        val us = userStorage.userSettings

        toolbar.setTitle(R.string.title_my_day)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val firstTime = us.firstTime
        if (firstTime) {
            mainViewModel.createDefaultTrackItems()
        } else {
            userStorage.updateSettings {
                it.firstTime = false
            }
        }

        adapter = TemplatesAdapter(this, mainViewModel)
        itemGrid.adapter = adapter
    }


    private fun createNewTrackItem() {
        startActivity<TrackItemCreatorActivity>()
        finish()
    }


    private fun save() {
        var trackItem: TrackItem

        // TODO save all track items
    }

    fun createViewModel(): MainViewModel {
        return ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

}
