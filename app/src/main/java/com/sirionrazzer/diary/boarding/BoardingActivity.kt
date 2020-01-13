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
import com.sirionrazzer.diary.models.UserStorage
import com.sirionrazzer.diary.util.StringUtils
import kotlinx.android.synthetic.main.activity_boarding.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class BoardingActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var userStorage: UserStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Diary.app.appComponent.inject(this)

        setContentView(R.layout.activity_boarding)

        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.app_name)
        toolbar.visibility = View.GONE

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        authViewModel.isLoggedIn.observe(this, Observer {
            if (it) {
                try {
                    Diary.app.installEncryptedRealm(authViewModel.getEncryptedPassword())

                    if (!userStorage.userSettings.boardingPickerShown && authViewModel.isNewcomer.value!!) {
                        startActivity(Intent(this, BoardingPickerActivity::class.java))
                    } else {
                        startActivity(Intent(this, HistoryActivity::class.java))
                    }
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Encryption has failed, reinstall needed",
                        Toast.LENGTH_SHORT
                    ).show()
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
            login()
        }

        signUpBtn.setOnClickListener {
            register()
        }

        anonymousRegisterBtn.setOnClickListener {
            authViewModel.anonymousRegister()
        }
    }

    override fun onResume() {
        super.onResume()

        if (userStorage.userSettings.accountCreated) {
            tvSubtitle.text = getString(R.string.insert_stored_credentials)
            tvSignInCaption.visibility = View.GONE
            signUpBtn.visibility = View.GONE
            signUpBtn.isEnabled = false
            anonymousRegisterBtn.visibility = View.GONE
            anonymousRegisterBtn.isEnabled = false
        }
    }

    private fun login() {
        val email = etEmail.text.toString()
        val pw = etPassword.text.toString()
        if (pw.isBlank() || pw.length < 6) {
            tilPassword.error = getString(R.string.short_password)
            Toast.makeText(this, getString(R.string.short_password), Toast.LENGTH_SHORT).show()
        } else if (StringUtils.isValidEmail(email)) {
            userStorage.userSettings.email.let {
                // TODO hack: no new user allowed, due to the db being prepared for the given user (might be fixed with new db for each new logged in account)
                if (it == null || it == email) {
                    authViewModel.login(email, pw)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.user_change_not_allowed),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.invalid_pa_email), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun register() {
        val email = etEmail.text.toString()
        val pw = etPassword.text.toString()
        if (pw.isBlank() || pw.length < 6) {
            tilPassword.error = getString(R.string.short_password)
            Toast.makeText(this, getString(R.string.short_password), Toast.LENGTH_SHORT).show()
        } else if (StringUtils.isValidEmail(email)) {
            if (!authViewModel.accountCreated.value!!) {
                authViewModel.register(email, pw)
            }
        } else {
            Toast.makeText(this, getString(R.string.invalid_pa_email), Toast.LENGTH_SHORT)
                .show()
        }
    }
}
