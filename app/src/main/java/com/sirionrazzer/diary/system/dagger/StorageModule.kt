package com.sirionrazzer.diary.system.dagger

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.sirionrazzer.diary.Diary
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class StorageModule(val app: Application) {

    @Provides
    @Singleton
    @Named("user_prefs")
    fun provideUserPrefs(): SharedPreferences =
        Diary.app.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
}