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
import main.java.com.sirionrazzer.diary.boarding.AuthViewModel
import javax.inject.Inject

class BoardingActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var userStorage: UserStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Diary.app.appComponent.inject(this)

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
            } else if (StringUtils.isValidEmail(email)) {
                if (!authViewModel.accountCreated.value!!) {
                    authViewModel.register(email, pw)
                } else {
                    // TODO hack: no new user allowed, due to the db being prepared for the given user (might be fixed with new db for each new logged in account)
                    userStorage.userSettings.email.let {
                        if (it != null && it == email) {
                            authViewModel.register(email, pw)
                        } else {
                            Toast.makeText(
                                this,
                                getString(R.string.user_change_not_allowed),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.invalid_pa_email), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        anonymousRegisterBtn.setOnClickListener {
            authViewModel.anonymousRegister()
        }

        if (userStorage.userSettings.accountCreated) {
            tvSubtitle.text = getString(R.string.insert_stored_credentials)
            anonymousRegisterBtn.visibility = View.GONE
            anonymousRegisterBtn.isEnabled = false
        }
    }
}
