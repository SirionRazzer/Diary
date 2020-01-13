package com.sirionrazzer.diary.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.boarding.AuthError
import com.sirionrazzer.diary.history.HistoryActivity
import com.sirionrazzer.diary.util.StringUtils
import kotlinx.android.synthetic.main.activity_boarding.etEmail
import kotlinx.android.synthetic.main.activity_boarding.etPassword
import kotlinx.android.synthetic.main.activity_boarding.signInBtn
import kotlinx.android.synthetic.main.activity_link_anonymous.*
import kotlinx.android.synthetic.main.toolbar.*
import com.sirionrazzer.diary.boarding.AuthViewModel
import com.sirionrazzer.diary.util.Result

class LinkAnonymousAccountActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_link_anonymous)
        setSupportActionBar(toolbar)
        toolbar.visibility = View.GONE

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        authViewModel.isAnonymous.observe(this, Observer {
            if (!it) {
                val res = Diary.app.reencryptRealm(authViewModel.getEncryptedPassword())
                if (res is Result.Error) {
                    Toast.makeText(this, "Error: ${res}", Toast.LENGTH_SHORT).show()
                } else {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    finish()
                }
            }
        })

        authViewModel.authError.observe(this, Observer {
            tilEmail.isErrorEnabled = true
            tilPassword.isErrorEnabled = true
            tilEmail.error = ""
            tilEmail.error = ""
            when (authViewModel.authErrorType.value) {
                AuthError.ERROR_CREDENTIAL_ALREADY_IN_USE -> tilPassword.error = it ?: ""
                AuthError.ERROR_FIREBASE_ERROR -> tilPassword.error = it ?: ""
                AuthError.ERROR_INVALID_CREDENTIALS -> tilPassword.error = it ?: ""
                AuthError.ERROR_INVALID_USER -> tilPassword.error = it ?: ""
                AuthError.ERROR_NETWORK_ERROR -> tilPassword.error = it ?: ""
                AuthError.ERROR_RECENT_LOGIN_REQUIRED -> tilPassword.error = it ?: ""
                AuthError.ERROR_WEAK_PASSWORD -> tilPassword.error = it ?: ""
                AuthError.NONE -> {
                    tilEmail.isErrorEnabled = false
                    tilPassword.isErrorEnabled = false
                }
                else -> {
                    tilEmail.isErrorEnabled = true
                    tilEmail.error = it ?: ""
                    tilPassword.isErrorEnabled = true
                    tilPassword.error = it ?: ""
                }
            }
        })

        signInBtn.setOnClickListener {
            val email = etEmail.text.toString()
            val pw = etPassword.text.toString()
            if (pw.isBlank() || pw.length < 6) {
                tilPassword.error = getString(R.string.short_password)
            } else if (StringUtils.isValidEmail(email)) {
                authViewModel.linkAnonymousToPassword(email, pw)
            } else {
                Toast.makeText(this, getString(R.string.invalid_pa_email), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        notNowBtn.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HistoryActivity::class.java))
        finish()
        super.onBackPressed()
    }
}