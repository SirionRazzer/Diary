package com.sirionrazzer.diary.history

import androidx.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.boarding.BoardingActivity
import com.sirionrazzer.diary.main.MainActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import java.io.File

class HistoryActivity : AppCompatActivity() {

    lateinit var historyViewModel: HistoryViewModel

    private lateinit var popupMenu: PopupMenu
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyViewModel = createViewModel()

        setSupportActionBar(toolbar)
        toolbar.title = ""

        val viewManager = LinearLayoutManager(this)
        val viewAdapter = HistoryAdapter(this, historyViewModel)
        val recyclerView = findViewById<RecyclerView>(R.id.historyItemRecyclerView)
        recyclerView.adapter = viewAdapter
        recyclerView.layoutManager = viewManager

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            startActivity<MainActivity>()
        }
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
