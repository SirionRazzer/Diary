package com.sirionrazzer.diary.system.dagger

import android.content.Context
import android.content.SharedPreferences
import com.sirionrazzer.diary.Diary
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    @Singleton
    @Named("user_prefs")
    fun provideUserPrefs(): SharedPreferences =
        Diary.app.getSharedPreferences("users", Context.MODE_PRIVATE)

}