package com.sirionrazzer.diary.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.main.MainViewModel
import com.sirionrazzer.diary.models.TrackItemDao
import io.realm.Realm
import kotlinx.android.synthetic.main.toolbar.*


class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.settings,
                SettingsFragment()
            )
            .commit()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        lateinit var mainViewModel: MainViewModel

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
            val button = findPreference<Preference>(getString(R.string.deleteHistoryButton))
            button!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                mainViewModel.deleteAllTrackItems()
                Toast.makeText(context, getString(R.string.history_deleted), Toast.LENGTH_SHORT).show()
                true
            }
        }
    }
}

private val Realm.trackItemDao: TrackItemDao
    get() {
        return TrackItemDao(this)
    }