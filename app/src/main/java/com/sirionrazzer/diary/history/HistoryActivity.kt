package com.sirionrazzer.diary.history

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sirionrazzer.diary.R
import kotlinx.android.synthetic.main.toolbar.*

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbar)
        toolbar.title = ""
    }
}