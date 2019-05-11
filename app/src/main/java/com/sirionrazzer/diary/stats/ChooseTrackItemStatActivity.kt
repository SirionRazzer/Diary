package com.sirionrazzer.diary.stats

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.main.MainViewModel
import kotlinx.android.synthetic.main.activity_choose_track_item_stat.*
import kotlinx.android.synthetic.main.toolbar.*

class ChooseTrackItemStatActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: TemplatesStatsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_track_item_stat)
        toolbar.title = getString(R.string.choose_activity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mainViewModel = createViewModel()
        adapter = TemplatesStatsAdapter(this, mainViewModel)
        gwTemplateStat.adapter = adapter

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        onBackPressed()
        return true
    }

    private fun createViewModel(): MainViewModel {
        return ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
}
