package com.sirionrazzer.diary.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.common.api.Api
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.models.UserStorage
import com.sirionrazzer.diary.trackitem.TrackItemCreatorActivity
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userStorage: UserStorage

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Diary.app.appComponent.inject(this)

        val us = userStorage.userSettings

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val firstTime = us.firstTime
        if (firstTime) {
            mainViewModel.createDefaultTrackItems()
        } else {
            userStorage.updateSettings {
                it.firstTime = false
            }
        }

        // TODO fill ListView

    }


    private fun createNewTrackItem() {
        startActivity<TrackItemCreatorActivity>()
        finish()
    }


    private fun save() {
        var trackItem: TrackItem

        // TODO save all track items
    }

}
