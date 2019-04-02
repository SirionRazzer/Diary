package com.sirionrazzer.diary.models

import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Named


class UserStorage @Inject constructor() {

    @Inject
    lateinit var gson: Gson

    @Inject
    @field:Named("user_prefs")
    lateinit var prefs: SharedPreferences


    var user: User?
        get() = loadUser()
        set(value) = storeUser(value!!)


    var userSettings: LocalUserSettings
        get() {
            val json = prefs.getString("user_settings", null)
            return if (json == null) LocalUserSettings() else gson.fromJson(json, LocalUserSettings::class.java)
        }
        set(value) =
            prefs.edit().putString("user_settings", gson.toJson(value)).apply()


    fun storeUser(user: User) {
        prefs.edit().putString("user", gson.toJson(user)).apply()
    }


    fun loadUser(): User? {
        val json = prefs.getString("user", null) ?: return null
        return gson.fromJson(json, User::class.java)
    }


    fun clearUserData() {
        prefs.edit().clear().apply()
    }


    fun updateSettings(updateFunc: (LocalUserSettings) -> Unit) {
        userSettings = userSettings.apply {
            updateFunc(this)
        }
    }
}
