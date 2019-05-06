package com.sirionrazzer.diary.creator

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.UserStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_templateitem_creator.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import javax.inject.Inject

class TemplateItemCreatorActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, ImagePickerDialog.ImagePickerDialogListener {

    private val TEXT_EXTRA = 1
    private val NUMBER_EXTRA = 2

    @Inject
    lateinit var userStorage: UserStorage

    lateinit var fragment: ImagePickerDialog

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

        ibImage.setOnClickListener {
            val fm = supportFragmentManager
            fragment = ImagePickerDialog(creatorViewModel)
            fragment.show(fm, resources.getString(R.string.choose_icon))
        }
    }


    override fun onResume() {
        super.onResume()
        if (creatorViewModel.hasChanged) {
            if (creatorViewModel.template.hasTextField) spExtra.setSelection(TEXT_EXTRA)
            if (creatorViewModel.template.hasNumberField) spExtra.setSelection(NUMBER_EXTRA)
            Picasso.get().load(creatorViewModel.template.imageOn).into(ibImage)
            etName.setText(creatorViewModel.template.name)
        }
    }


    override fun onPause() {
        super.onPause()
        creatorViewModel.template.name = etName.text.toString()
    }


    private fun createViewModel(): TemplateItemCreatorViewModel {
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

            val intent = Intent()
            if (creatorViewModel.hasChanged) {
                setResult(0, intent)
            } else {
                setResult(1, intent)
            }

            finish()
        } else {
            onBackPressed()
        }

        return true
    }


    override fun onBackPressed() {
        if (creatorViewModel.hasChanged || etName.text!!.toString().isNotEmpty()) {
            alert(getString(R.string.message_leave_without_save), getString(R.string.caption_activity_not_saved)) {
                yesButton {
                    setResult(1, intent)
                    super.onBackPressed()
                }
            }.show()
        } else {
            setResult(1, intent)
            super.onBackPressed()
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>) {
        creatorViewModel.template.hasNumberField = false
        creatorViewModel.template.hasTextField = false
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (position) {
            TEXT_EXTRA -> {
                creatorViewModel.template.hasNumberField = false
                creatorViewModel.template.hasTextField = true
                creatorViewModel.hasChanged = true
            }
            NUMBER_EXTRA -> {
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


    override fun onImagePicked(dialog: DialogFragment) {
        dialog.dismiss()
        Picasso.get().load(creatorViewModel.template.imageOn).into(ibImage)
        creatorViewModel.hasChanged = true
    }
}
