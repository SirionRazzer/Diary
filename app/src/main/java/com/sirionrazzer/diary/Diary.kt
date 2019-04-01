package com.sirionrazzer.diary

import android.app.Application
import com.sirionrazzer.diary.system.dagger.AppComponent
import com.sirionrazzer.diary.system.dagger.DaggerAppComponent
import io.realm.Realm

class Diary : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .build()
    }


    override fun onCreate() {
        super.onCreate()

        app = this

        //init realmdb this covers all use of realm within the project.
        Realm.init(this)
    }


    companion object {
        lateinit var app: Diary

    }

}
