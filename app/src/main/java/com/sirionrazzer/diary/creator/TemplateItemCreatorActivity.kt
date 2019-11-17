package com.sirionrazzer.diary.creator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.models.UserStorage
import com.sirionrazzer.diary.viewer.TemplateItemViewerActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_templateitem_creator.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import javax.inject.Inject

class TemplateItemCreatorActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    ImagePickerDialog.ImagePickerDialogListener {

    companion object {
        private val TEXT_EXTRA = 1
        private val NUMBER_EXTRA = 2
    }

    @Inject
    lateinit var userStorage: UserStorage

    private lateinit var fragment: ImagePickerDialog
    private lateinit var viewModel: TemplateItemCreatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_templateitem_creator)

        Diary.app.appComponent.inject(this)

        viewModel = createViewModel()

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
            fragment = ImagePickerDialog(viewModel)
            fragment.show(fm, resources.getString(R.string.choose_icon))
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.hasChanged.value != null && viewModel.hasChanged.value!! && createViewModel().template.value != null) {
            if (viewModel.template.value!!.hasTextField) spExtra.setSelection(TEXT_EXTRA)
            if (viewModel.template.value!!.hasNumberField) spExtra.setSelection(NUMBER_EXTRA)
            Picasso.get().load(viewModel.template.value!!.image).into(ibImage)
            etName.setText(viewModel.template.value!!.name)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.template.value?.name = etName.text.toString()
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
            if (!etName.text.toString().isBlank()) {
                viewModel.template.value?.name = etName.text.toString()
                viewModel.hasChanged.value = true
                viewModel.saveNewTemplate()
                val intent = Intent()
                setResult(TemplateItemViewerActivity.CHANGE, intent)
                finish()
            } else {
                // TODO show warning
            }
        } else {
            onBackPressed()
        }
        return true
    }

    override fun onBackPressed() {
        if ((viewModel.hasChanged.value != null && viewModel.hasChanged.value!!) || etName.text!!.toString().isNotEmpty()) {
            alert(
                getString(R.string.message_leave_without_save),
                getString(R.string.caption_activity_not_saved)
            ) {
                yesButton {
                    setResult(TemplateItemViewerActivity.NOCHANGE, intent)
                    super.onBackPressed()
                }
            }.show()
        } else {
            setResult(TemplateItemViewerActivity.NOCHANGE, intent)
            super.onBackPressed()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        viewModel.template.value?.hasNumberField = false
        viewModel.template.value?.hasTextField = false
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (position) {
            TEXT_EXTRA -> {
                viewModel.template.value?.hasNumberField = false
                viewModel.template.value?.hasTextField = true
                viewModel.hasChanged.value = true
            }
            NUMBER_EXTRA -> {
                viewModel.template.value?.hasNumberField = true
                viewModel.template.value?.hasTextField = false
                viewModel.hasChanged.value = true
            }
            else -> {
                viewModel.template.value?.hasNumberField = false
                viewModel.template.value?.hasTextField = false
            }
        }
    }

    override fun onImagePicked(dialog: DialogFragment) {
        dialog.dismiss()
        if (createViewModel().template.value != null) {
            Picasso.get().load(viewModel.template.value!!.image).into(ibImage)
        }
        viewModel.hasChanged.value = true
    }
}
