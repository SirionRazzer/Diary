package com.sirionrazzer.diary.trackitem

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import kotlinx.android.synthetic.main.activity_templateitem_viewer.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity

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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_create_activity_template, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mAddTemplate) {
            startActivity<TemplateItemCreatorActivity>()
        } else {
            onBackPressed()
        }
        return true
    }


    override fun onBackPressed() {
        // TODO change result also if user created new activity!

        var intent = Intent()

        if (tiViewModel.hasChanged) {
            setResult(0, intent)
        } else {
            setResult(1, intent)
        }

        finish()
    }
}