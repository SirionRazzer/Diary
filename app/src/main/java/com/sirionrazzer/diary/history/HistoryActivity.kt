package com.sirionrazzer.diary.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.boarding.BoardingActivity
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.profile.LinkAnonymousAccountActivity
import com.sirionrazzer.diary.settings.SettingsActivity
import com.sirionrazzer.diary.stats.ChooseTrackItemStatActivity
import com.sirionrazzer.diary.util.DateUtils
import kotlinx.android.synthetic.main.activity_history.*
import main.java.com.sirionrazzer.diary.boarding.AuthViewModel
import org.jetbrains.anko.startActivity
import org.threeten.bp.LocalDate
import kotlin.math.abs


class HistoryActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private val PERCENTAGE_TO_SHOW_ABL_CONTENT = 20
    private var maxScrollSize: Int = 0

    lateinit var authViewModel: AuthViewModel
    lateinit var historyViewModel: HistoryViewModel
    lateinit var viewAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)

        cToolbar.setTitle(R.string.title_history_activity)
        setSupportActionBar(cToolbar)
        cab.title = getString(R.string.title_history_activity)

        ab.addOnOffsetChangedListener(this)

        viewAdapter = HistoryAdapter(this, historyViewModel)

        rvHistoryItems.adapter = viewAdapter
        rvHistoryItems.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        fab.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("editDate", LocalDate.now().toEpochDay() * DateUtils.DAY_MILLISECONDS)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        refreshUI()
    }

    override fun onDestroy() {
        Log.d(HistoryActivity::class.java.simpleName, "Desroyed")
        super.onDestroy()
    }

    private fun refreshUI() {
        historyViewModel.loadData()
        viewAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (FirebaseAuth.getInstance().currentUser != null) {
            menuInflater.inflate(R.menu.history_popup_menu, menu)
        } else {
            menuInflater.inflate(R.menu.history_login_menu, menu)
        }
        return true
    }

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        val isAnonymousUser = authViewModel.isAnonymous.value as Boolean
        val logoutBtn = menu?.findItem(R.id.logout_button)
        val createAccBtn = menu?.findItem(R.id.create_account_button)
        val syncBtn = menu?.findItem(R.id.sync_button)
        val downloadBtn = menu?.findItem(R.id.download_button)
        if (isAnonymousUser) {
            logoutBtn?.isEnabled = false
            syncBtn?.isEnabled = false
            downloadBtn?.isEnabled = false
        } else {
            createAccBtn?.isVisible = false
            createAccBtn?.isEnabled = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {

            val filePath = "/user/${user.uid}/backup/realm.json"
            when (item?.itemId) {
                R.id.sync_button -> {
                    Toast.makeText(
                        this,
                        "Synchronization will be enabled in the next update, stay tuned! :) ",
                        Toast.LENGTH_SHORT
                    ).show()
//                    val storageRef = FirebaseStorage.getInstance().reference
//                    val byteData = historyViewModel.getJsonData()
//                    val fileRef = storageRef.child(filePath)
//                    fileRef.putBytes(byteData).addOnSuccessListener {
//                        Toast.makeText(
//                            this,
//                            getString(R.string.success_backup_upload),
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                    }.addOnFailureListener {
//                        Toast.makeText(
//                            this,
//                            getString(R.string.error_backup_upload),
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }

                }
                R.id.download_button -> {
                    Toast.makeText(
                        this,
                        "Synchronization will be enabled in the next update, stay tuned! :) ",
                        Toast.LENGTH_SHORT
                    ).show()
//                    val storageRef = FirebaseStorage.getInstance().reference
//                    val file = storageRef.child(filePath)
//
//                    val ONE_MEGABYTE: Long = 1024 * 1024
//                    file.getBytes(ONE_MEGABYTE).addOnSuccessListener {
//                        historyViewModel.reloadDataFromBytes(it)
//                        refreshUI()
//                        historyViewModel.userStorage.updateSettings { lus ->
//                            lus.firstTime = false
//                        }
//                        Toast.makeText(
//                            this,
//                            getString(R.string.success_backup_download),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }.addOnFailureListener {
//                        Toast.makeText(
//                            this,
//                            getString(R.string.error_backup_download),
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
                }
                R.id.logout_button -> {
                    // TODO handle realm DB renaming and recreating if another user tries to sign in (works ok for the same user)
                    FirebaseAuth.getInstance().signOut()
                    authViewModel.isLoggedIn.value = false
                    startActivity<BoardingActivity>()
                    finish()
                    Toast.makeText(this, getString(R.string.success_logged_out), Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.options_button -> {
                    startActivity<SettingsActivity>()
                }
                R.id.stats_button -> {
                    startActivity<ChooseTrackItemStatActivity>()
                }
                R.id.create_account_button -> {
                    startActivity<LinkAnonymousAccountActivity>()
                    historyViewModel.closeRealms() // HACK to clean all realms in history view model when user possibly will reencrypt realms with new account
                    finish()
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

    override fun onOffsetChanged(abl: AppBarLayout?, i: Int) {
        if (maxScrollSize == 0) maxScrollSize = abl!!.totalScrollRange
        val currentScrollPercentage = (abs(i)) * 100
        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_ABL_CONTENT) {
            ViewCompat.animate(fab).scaleY(0f).scaleX(0f).start()
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_ABL_CONTENT) {
            ViewCompat.animate(fab).scaleY(1f).scaleX(1f).start()
        }
    }
}