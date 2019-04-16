package com.sirionrazzer.diary.trackitem

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.UserStorage
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class TemplateItemCreatorActivity : AppCompatActivity() {

    @Inject
    lateinit var userStorage: UserStorage

    lateinit var creatorViewModel: TemplateItemCreatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_templateitem_creator)

        Diary.app.appComponent.inject(this)

        creatorViewModel = createViewModel()

        toolbar.setTitle(R.string.title_my_day)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }


    fun createViewModel(): TemplateItemCreatorViewModel {
        return ViewModelProviders.of(this).get(TemplateItemCreatorViewModel::class.java)
    }

}