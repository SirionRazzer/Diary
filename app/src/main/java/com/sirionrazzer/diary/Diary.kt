package com.sirionrazzer.diary

import android.app.Application
import com.sirionrazzer.diary.system.dagger.ApiModule
import com.sirionrazzer.diary.system.dagger.AppComponent
import com.sirionrazzer.diary.system.dagger.DaggerAppComponent
import com.sirionrazzer.diary.system.dagger.StorageModule
import io.realm.Realm

class Diary : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .apiModule(ApiModule())
            .storageModule(StorageModule(this))
            .build()
    }


    override fun onCreate() {
        super.onCreate()

        //init realmdb this covers all use of realm within the project.
        Realm.init(this)

        app = this
    }


    companion object {
        lateinit var app: Diary
    }
}
