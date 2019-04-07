package com.sirionrazzer.diary.trackitem

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import kotlinx.android.synthetic.main.toolbar.*

class TrackItemViewerActivity : AppCompatActivity() {

    lateinit var tiViewModel: TrackItemViewerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trackitem_viewer)


        Diary.app.appComponent.inject(this)

        tiViewModel = createViewModel()

        toolbar.setTitle(R.string.title_manage_activities)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }


    fun createViewModel(): TrackItemViewerViewModel {
        return ViewModelProviders.of(this).get(TrackItemViewerViewModel::class.java)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}