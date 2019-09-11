package com.sirionrazzer.diary.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.boarding.BoardingActivity
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.settings.SettingsActivity
import com.sirionrazzer.diary.stats.ChooseTrackItemStatActivity
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity

class HistoryActivity : AppCompatActivity() {

    lateinit var historyViewModel: HistoryViewModel

    lateinit var viewAdapter: HistoryAdapter

    private lateinit var popupMenu: PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyViewModel = createViewModel()

        toolbar.setTitle(R.string.title_history_activity)
        setSupportActionBar(toolbar)

        val viewManager = LinearLayoutManager(this)
        viewAdapter = HistoryAdapter(this, historyViewModel)

        rvHistoryItems.adapter = viewAdapter
        rvHistoryItems.layoutManager = viewManager

        fab.setOnClickListener {
            startActivity<MainActivity>()
        }
    }

    override fun onStart() {
        super.onStart()
        refreshUI()
    }

    private fun refreshUI() {
        historyViewModel.loadData()
        viewAdapter.notifyDataSetChanged()
    }

    private fun createViewModel(): HistoryViewModel {
        return ViewModelProviders.of(this).get(HistoryViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (FirebaseAuth.getInstance().currentUser != null) {
            menuInflater.inflate(R.menu.history_popup_menu, menu)
        } else {
            menuInflater.inflate(R.menu.history_login_menu, menu)
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {

            val filePath = "/user/${user.uid}/backup/realm.json"
            when {
                item?.itemId == R.id.sync_button -> {
                    val storageRef = FirebaseStorage.getInstance().reference
                    val byteData = historyViewModel.getJsonData()
                    val fileRef = storageRef.child(filePath)
                    fileRef.putBytes(byteData).addOnSuccessListener {
                        Toast.makeText(
                            this,
                            getString(R.string.success_backup_upload),
                            Toast.LENGTH_SHORT
                        ).show()

                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            getString(R.string.error_backup_upload),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
                item?.itemId == R.id.download_button -> {
                    val storageRef = FirebaseStorage.getInstance().reference
                    val file = storageRef.child(filePath)

                    val ONE_MEGABYTE: Long = 1024 * 1024
                    file.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        historyViewModel.reloadDataFromBytes(it)
                        refreshUI()
                        historyViewModel.userStorage.updateSettings { lus ->
                            lus.firstTime = false
                        }
                        Toast.makeText(
                            this,
                            getString(R.string.success_backup_download),
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            getString(R.string.error_backup_download),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                item?.itemId == R.id.logout_button -> {

                    FirebaseAuth.getInstance().signOut()
                    startActivity<BoardingActivity>()
                    finish()
                    Toast.makeText(this, getString(R.string.success_logged_out), Toast.LENGTH_SHORT)
                        .show()
                }
                item?.itemId == R.id.options_button -> {
                    startActivity<SettingsActivity>()
                }
                item?.itemId == R.id.stats_button -> {
                    startActivity<ChooseTrackItemStatActivity>()
                }
                else -> Toast.makeText(
                    this,
                    getString(R.string.please_backup),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            if (item?.itemId == R.id.login_history_button) {
                startActivity<BoardingActivity>()
                finish()
            } else if (item?.itemId == R.id.login_options_button) {
                startActivity<SettingsActivity>()
            }
        }
        return true
    }
}