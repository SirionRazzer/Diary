package com.sirionrazzer.diary.trackitem

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import kotlinx.android.synthetic.main.activity_templateitem_viewer.*
import kotlinx.android.synthetic.main.toolbar.*

class TemplateItemViewerActivity : AppCompatActivity() {

    lateinit var tiViewModel: TemplateItemViewerViewModel
    lateinit var adapter: TemplateViewerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_templateitem_viewer)


        Diary.app.appComponent.inject(this)

        tiViewModel = createViewModel()

        toolbar.setTitle(R.string.title_manage_activities)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        adapter = TemplateViewerAdapter(this, tiViewModel)
        rvTemplates.adapter = adapter
        rvTemplates.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    fun createViewModel(): TemplateItemViewerViewModel {
        return ViewModelProviders.of(this).get(TemplateItemViewerViewModel::class.java)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}