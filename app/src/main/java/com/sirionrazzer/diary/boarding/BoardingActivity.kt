package com.sirionrazzer.diary.boarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.history.HistoryActivity
import kotlinx.android.synthetic.main.activity_boarding.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity


class BoardingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        val auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_boarding)

        login()
        setSupportActionBar(toolbar)
        toolbar.title = "Diary"
        toolbar.visibility = View.GONE

        signIn.setOnClickListener {
            val email = etEmail.text.toString()
            val pw = etPassword.text.toString()
            if (isValidEmail(email) && !pw.isBlank()) {
                auth.signInWithEmailAndPassword(email, pw)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            login()
                            //TODO: load stuff from firebase save
                        } else {
                            val e = task.exception as FirebaseAuthException
                            if (e.errorCode == "ERROR_WRONG_PASSWORD") {
                                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                            } else {
                                showDialog(auth, email, pw)
                            }
                        }
                    }

            } else {
                Toast.makeText(this, getString(R.string.invalid_pa_email), Toast.LENGTH_SHORT)
                    .show()

            }
        }

        skipSignIn.setOnClickListener {
            startActivity<HistoryActivity>()
            finish()
        }


    }

    private fun showDialog(auth: FirebaseAuth, email: String, pw: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Would you like to create a new account? If you are not new, then your credentials were incorrect.")
            .setTitle("Hey there")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener {
                    Toast.makeText(this, "Thank you for signing up", Toast.LENGTH_LONG).show()
                    startActivity<HistoryActivity>()
                    finish()

                }
            }
            .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
        builder.show()
    }

    private fun isValidEmail(email: String): Boolean {
        return if (email.isBlank()) false else android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun login() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivity(Intent(this, HistoryActivity::class.java))
            finish()
            Toast.makeText(this, "You've been successfully logged in!", Toast.LENGTH_SHORT).show()
        }
    }

}
