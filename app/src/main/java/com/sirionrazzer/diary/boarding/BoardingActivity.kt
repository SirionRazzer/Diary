package com.sirionrazzer.diary.boarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.history.HistoryActivity
import kotlinx.android.synthetic.main.activity_boarding.*
import kotlinx.android.synthetic.main.toolbar.*
import main.java.com.sirionrazzer.diary.boarding.AuthViewModel

class BoardingActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        authViewModel.isLoggedIn.observe(this, Observer {
            if (it) {
                Diary.app.installEncryptedRealm(authViewModel.getEncryptedPassword())
                startActivity(Intent(this, HistoryActivity::class.java))
                finish()
            }
        })
        setContentView(R.layout.activity_boarding)

        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.app_name)
        toolbar.visibility = View.GONE

        signInBtn.setOnClickListener {
            val email = etEmail.text.toString()
            val pw = etPassword.text.toString()
            if (pw.isBlank() || pw.length < 6) {
                Toast.makeText(this, getString(R.string.short_password), Toast.LENGTH_SHORT).show()
            } else if (isValidEmail(email)) {
                authViewModel.register(email, pw)
            } else {
                Toast.makeText(this, getString(R.string.invalid_pa_email), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        anonymousRegisterBtn.setOnClickListener {
            authViewModel.anonymousRegister()
        }
    }


    private fun isValidEmail(email: String): Boolean {
        return if (email.isBlank()) false else android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
