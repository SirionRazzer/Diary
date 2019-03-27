package com.sirionrazzer.diary

import android.app.Application
import com.sirionrazzer.diary.system.dagger.AppComponent
import com.sirionrazzer.diary.system.dagger.DaggerAppComponent

class Diary : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .build()
    }
    override fun onCreate() {
        super.onCreate()

        app = this
    }

    companion object {
        lateinit var app: Diary
    }
}
