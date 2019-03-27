package com.sirionrazzer.diary.boarding

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.UserStorage
import javax.inject.Inject

class BoardingViewModel: ViewModel() {

    @Inject
    lateinit var userStorage: UserStorage

    init {
        Diary.app.appComponent.inject(this)
    }
}