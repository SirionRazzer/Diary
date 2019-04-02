package com.sirionrazzer.diary.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.TrackItem
import com.sirionrazzer.diary.models.UserStorage
import com.sirionrazzer.diary.trackitem.TrackItemCreatorActivity
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var userStorage: UserStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val firstTime = userStorage.userSettings.firstTime
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


    private fun saveTrackItem(trackItem: TrackItem) {
        // TODO
    }

}
