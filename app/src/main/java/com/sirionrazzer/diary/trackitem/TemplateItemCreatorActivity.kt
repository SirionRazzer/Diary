package com.sirionrazzer.diary.trackitem

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.UserStorage
import kotlinx.android.synthetic.main.activity_templateitem_creator.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import javax.inject.Inject

class TemplateItemCreatorActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    @Inject
    lateinit var userStorage: UserStorage

    lateinit var creatorViewModel: TemplateItemCreatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_templateitem_creator)

        Diary.app.appComponent.inject(this)

        creatorViewModel = createViewModel()

        toolbar.setTitle(R.string.title_creator_activity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        // set spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.input_fields,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spExtra.adapter = adapter
            spExtra.onItemSelectedListener = this
        }
    }


    fun createViewModel(): TemplateItemCreatorViewModel {
        return ViewModelProviders.of(this).get(TemplateItemCreatorViewModel::class.java)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_save_template, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mCreateTemplate) {
            creatorViewModel.template.name = etName.text.toString()
            creatorViewModel.saveNewTemplate()
            finish()
        } else {
            onBackPressed()
        }

        return true
    }


    override fun onBackPressed() {
        if (creatorViewModel.hasChanged || etName.text!!.toString().isNotEmpty()) {
            alert(getString(R.string.message_leave_without_save), getString(R.string.caption_activity_not_saved)) {
                yesButton { super.onBackPressed() }
            }.show()
        } else {
            super.onBackPressed()
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>) {
        creatorViewModel.template.hasNumberField = false
        creatorViewModel.template.hasTextField = false
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (position) {
            1 -> {
                creatorViewModel.template.hasNumberField = false
                creatorViewModel.template.hasTextField = true
                creatorViewModel.hasChanged = true
            }
            2 -> {
                creatorViewModel.template.hasNumberField = true
                creatorViewModel.template.hasTextField = false
                creatorViewModel.hasChanged = true
            }
            else -> {
                creatorViewModel.template.hasNumberField = false
                creatorViewModel.template.hasTextField = false
            }
        }
    }
}