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
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        setContentView(R.layout.activity_link_anonymous)
        setSupportActionBar(toolbar)
        toolbar.visibility = View.GONE

        signInBtn.setOnClickListener {
            val email = etEmail.text.toString()
            val pw = etPassword.text.toString()
            if (pw.isBlank() || pw.length < 6) {
                Toast.makeText(this, getString(R.string.short_password), Toast.LENGTH_SHORT).show()
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