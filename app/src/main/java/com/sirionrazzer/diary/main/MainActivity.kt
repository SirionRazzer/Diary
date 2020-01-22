package com.sirionrazzer.diary.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.UserStorage
import com.sirionrazzer.diary.util.DateUtils
import com.sirionrazzer.diary.util.DateUtils.Factory.DAY_MILLISECONDS
import com.sirionrazzer.diary.viewer.TemplateItemViewerActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userStorage: UserStorage

    lateinit var mainViewModel: MainViewModel
    var adapter: TemplatesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Diary.app.appComponent.inject(this)

        toolbar.setTitle(R.string.title_my_day)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        mainViewModel = createViewModel()
        mainViewModel.date.observe(this, Observer {
            tvDate.text = DateUtils.smartDate(
                DateUtils.dateFromMillis(it.toEpochDay() * DAY_MILLISECONDS),
                false
            )

            revertDayColors()
            when (it.dayOfWeek) {
                DayOfWeek.MONDAY -> tvMonday.setTextColor(resources.getColor(R.color.colorPrimary))
                DayOfWeek.TUESDAY -> tvTuesday.setTextColor(resources.getColor(R.color.colorPrimary))
                DayOfWeek.WEDNESDAY -> tvWednesday.setTextColor(resources.getColor(R.color.colorPrimary))
                DayOfWeek.THURSDAY -> tvThursday.setTextColor(resources.getColor(R.color.colorPrimary))
                DayOfWeek.FRIDAY -> tvFriday.setTextColor(resources.getColor(R.color.colorPrimary))
                DayOfWeek.SATURDAY -> tvSaturday.setTextColor(resources.getColor(R.color.colorPrimary))
                DayOfWeek.SUNDAY -> tvSunday.setTextColor(resources.getColor(R.color.colorPrimary))
                else -> tvMonday.setTextColor(resources.getColor(R.color.colorPrimary))
            }

            adapter?.notifyDataSetChanged()
        })

        val dateLong =
            intent.getLongExtra("editDate", LocalDate.now().toEpochDay() * DAY_MILLISECONDS)
        mainViewModel.setForDate(
            Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault()).toLocalDate()
        )

        adapter = TemplatesAdapter(this, mainViewModel)
        gwTemplates.adapter = adapter
    }

    private fun revertDayColors() {
        tvMonday.setTextColor(resources.getColor(R.color.secondary_text))
        tvTuesday.setTextColor(resources.getColor(R.color.secondary_text))
        tvWednesday.setTextColor(resources.getColor(R.color.secondary_text))
        tvThursday.setTextColor(resources.getColor(R.color.secondary_text))
        tvFriday.setTextColor(resources.getColor(R.color.secondary_text))
        tvSaturday.setTextColor(resources.getColor(R.color.secondary_text))
        tvSunday.setTextColor(resources.getColor(R.color.secondary_text))
    }

    override fun onResume() {
        super.onResume()

        tvDate.setOnClickListener {
            showDatePicker()
        }
        ivCalendar.setOnClickListener {
            showDatePicker()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == TemplateItemViewerActivity.CHANGE) { // templates are changed
            mainViewModel.setupTrackAndTemplateItems()
            adapter?.notifyDataSetChanged()

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

    fun showDatePicker() {
        DatePickerFragment().show(fragmentManager, "timePicker")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mSaveLog) {
            save()
            finish()
        } else if (item?.itemId == R.id.mManageActivities) {
            val intent = Intent(this, TemplateItemViewerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, 1)
        } else {
            onBackPressed()
        }
        return true
    }
}
