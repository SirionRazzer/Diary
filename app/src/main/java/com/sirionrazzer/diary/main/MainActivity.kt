package com.sirionrazzer.diary.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.UserStorage
import com.sirionrazzer.diary.util.DateUtils
import com.sirionrazzer.diary.viewer.TemplateItemViewerActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userStorage: UserStorage

    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: TemplatesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Diary.app.appComponent.inject(this)

        toolbar.setTitle(R.string.title_my_day)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        mainViewModel = createViewModel()
        mainViewModel.editedIds = intent.getStringArrayListExtra("trackItemsIds")
        mainViewModel.setupTrackAndTemplateItems()

        if (userStorage.userSettings.firstTime) {
            mainViewModel.createDefaultTrackItems()
            userStorage.updateSettings {
                it.firstTime = false
            }
        }

        adapter = TemplatesAdapter(this, mainViewModel)
        gwTemplates.adapter = adapter

        btnManageActivities.setOnClickListener {
            //startActivity<TemplateItemViewerActivity>()
            val intent = Intent(this, TemplateItemViewerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, 1)
        }
    }

    override fun onResume() {
        super.onResume()
        tvDate.text = DateUtils.smartDate(DateUtils.dateFromMillis(mainViewModel.date), false)

        when (DateUtils.dayInWeek()) {
            2 -> tvMonday.setTextColor(resources.getColor(R.color.colorAccent))
            3 -> tvTuesday.setTextColor(resources.getColor(R.color.colorAccent))
            4 -> tvWednesday.setTextColor(resources.getColor(R.color.colorAccent))
            5 -> tvThursday.setTextColor(resources.getColor(R.color.colorAccent))
            6 -> tvFriday.setTextColor(resources.getColor(R.color.colorAccent))
            0 -> tvSaturday.setTextColor(resources.getColor(R.color.colorAccent))
            1 -> tvSunday.setTextColor(resources.getColor(R.color.colorAccent))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == TemplateItemViewerActivity.CHANGE) { // templates are changed
            mainViewModel.setupTrackAndTemplateItems()
            adapter.notifyDataSetChanged()

            val snackbar =
                Snackbar.make(
                    clMain,
                    resources.getString(R.string.activities_updated),
                    Snackbar.LENGTH_SHORT
                )
            snackbar.show()
        }

    }

    private fun save() {
        mainViewModel.saveTrackItems()
    }

    fun createViewModel(): MainViewModel {
        return ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_save_log, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mSaveLog) {
            save()
            finish()
        } else {
            onBackPressed()
        }
        return true
    }
}
