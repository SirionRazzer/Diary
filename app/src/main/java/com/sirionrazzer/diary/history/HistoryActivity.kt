package com.sirionrazzer.diary.history

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.boarding.BoardingActivity
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.models.TrackItem
import io.realm.Realm
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import java.io.File

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var popupMenu: PopupMenu
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbar)
        toolbar.title = ""

        val item1 = TrackItem("1", false, "item 1", 1, 1, false, false, true, null, null, 123, 0)
        val item2 = TrackItem("2", false, "item 2", 1, 1, false, false, false, null, null, 123, 0)
        val item3 = TrackItem("3", false, "item 3", 1, 1, false, false, true, null, null, 123, 0)
        val item4 = TrackItem("4", false, "item 4", 1, 1, false, false, true, null, null, 123, 0)
        val item5 = TrackItem("5", false, "item 5", 1, 1, false, false, true, null, null, 123, 0)
        val item6 = TrackItem("6", false, "item 6", 1, 1, false, false, false, null, null, 123, 0)
        val item7 = TrackItem("7", false, "item 7", 1, 1, false, false, true, null, null, 123, 0)
        val item10 = TrackItem("10", false, "item 10", 1, 1, true, false, true, "some text", null, 123, 0)
        val item11 = TrackItem("11", false, "item 11", 1, 1, true, false, false, "some text", null, 123, 0)
        val item12 = TrackItem("12", false, "item 12", 1, 1, false, true, true, null, 42.toFloat(), 123, 0)
        val trackitems1: ArrayList<TrackItem> = arrayListOf(item1, item2, item3, item4, item5, item6, item7, item10)
        val trackitems2: ArrayList<TrackItem> = arrayListOf(item1, item2, item3)
        val trackitems3: ArrayList<TrackItem> = arrayListOf(item4, item5, item6, item7, item10, item11, item12)
        val historyItems: ArrayList<ArrayList<TrackItem>> = arrayListOf(trackitems1, trackitems2, trackitems3)

        viewManager = LinearLayoutManager(this)
        viewAdapter = HistoryAdapter(this, historyItems)
        recyclerView = findViewById(R.id.historyItemRecyclerView)
        recyclerView.adapter = viewAdapter
        recyclerView.layoutManager = viewManager

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            startActivity<MainActivity>()
        }
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

            val filePath = "/user/${user.uid}/backup/default.realm"
            when {
                item?.itemId == R.id.sync_button -> {
                    val storageRef = FirebaseStorage.getInstance().reference
                    val file = Uri.fromFile(File(realm.path))
                    val fileRef = storageRef.child(filePath)
                    fileRef.putFile(file).addOnSuccessListener { res ->
                        Toast.makeText(this, getString(R.string.success_backup_upload), Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener { res ->
                        Toast.makeText(this, getString(R.string.error_backup_upload), Toast.LENGTH_LONG).show()
                    }

                }
                item?.itemId == R.id.download_button -> {
                    val storageRef = FirebaseStorage.getInstance().reference
                    val file = storageRef.child(filePath)

                    val ONE_MEGABYTE: Long = 1024 * 1024
                    file.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        File(realm.path).writeBytes(it)
                        Toast.makeText(this, getString(R.string.success_backup_download), Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, getString(R.string.error_backup_download), Toast.LENGTH_LONG).show()
                    }
                    //TODO: refresh ui after download
                }
                item?.itemId == R.id.logout_button -> {

                    FirebaseAuth.getInstance().signOut()
                    startActivity<BoardingActivity>()
                    finish()
                    Toast.makeText(this, getString(R.string.success_logged_out), Toast.LENGTH_SHORT).show()
                }
                else -> Toast.makeText(this, getString(R.string.please_backup), Toast.LENGTH_SHORT).show()
            }
        } else {
            if (item?.itemId == R.id.login_history_button) {
                startActivity<BoardingActivity>()
                finish()
            }
        }
        return true
    }
}