package com.sirionrazzer.diary.main

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sirionrazzer.diary.R
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    lateinit var purchaseViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }




}
