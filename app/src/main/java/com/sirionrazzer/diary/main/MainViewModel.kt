package com.sirionrazzer.diary.main

import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.UserStorage
import javax.inject.Inject

class MainViewModel {
    @Inject
    lateinit var userStorage: UserStorage

    init {
        Diary.app.appComponent.inject(this)
    }
}