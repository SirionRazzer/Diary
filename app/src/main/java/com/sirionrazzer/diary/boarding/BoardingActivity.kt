package com.sirionrazzer.diary.boarding

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.history.HistoryActivity
import kotlinx.android.synthetic.main.activity_boarding.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity

class BoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_boarding)

        setSupportActionBar(toolbar)
        toolbar.title = "Diary"
        toolbar.visibility = View.GONE

        signIn.setOnClickListener{
            // TODO: store values of name and password
            startActivity<HistoryActivity>()
            finish()
        }

        skipSignIn.setOnClickListener{
            startActivity<HistoryActivity>()
            finish()
        }
    }

}
