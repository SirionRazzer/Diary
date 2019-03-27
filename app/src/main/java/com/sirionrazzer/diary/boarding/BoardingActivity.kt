package com.sirionrazzer.diary.boarding

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.history.HistoryActivity
import kotlinx.android.synthetic.main.activity_boarding.*
import org.jetbrains.anko.startActivity

class BoardingActivity : AppCompatActivity() {

    lateinit var boardingViewModel: BoardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        boardingViewModel = ViewModelProviders.of(this)[BoardingViewModel::class.java]

        setContentView(R.layout.activity_boarding)

        signIn.setOnClickListener{
            startActivity<HistoryActivity>()
            finish()
        }
    }
}