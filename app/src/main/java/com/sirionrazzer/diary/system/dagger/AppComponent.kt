package com.sirionrazzer.diary.system.dagger

import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.boarding.BoardingActivity
import com.sirionrazzer.diary.boarding.BoardingPickerActivity
import com.sirionrazzer.diary.creator.TemplateItemCreatorActivity
import com.sirionrazzer.diary.creator.TemplateItemCreatorViewModel
import com.sirionrazzer.diary.history.HistoryActivity
import com.sirionrazzer.diary.history.HistoryViewModel
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.main.MainViewModel
import com.sirionrazzer.diary.viewer.TemplateItemViewerActivity
import com.sirionrazzer.diary.viewer.TemplateItemViewerViewModel
import dagger.Component
import com.sirionrazzer.diary.boarding.AuthViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, StorageModule::class])
interface AppComponent {

    fun inject(diary: Diary)

    fun inject(authViewModel: AuthViewModel)
    fun inject(historyViewModel: HistoryViewModel)
    fun inject(templateItemViewerViewModel: TemplateItemViewerViewModel)
    fun inject(templateItemCreatorViewModel: TemplateItemCreatorViewModel)
    fun inject(mainViewModel: MainViewModel)

    fun inject(mainActivity: MainActivity)
    fun inject(boardingPickerActivity: BoardingPickerActivity)
    fun inject(boardingActivity: BoardingActivity)
    fun inject(historyActivity: HistoryActivity)
    fun inject(templateItemCreatorActivity: TemplateItemCreatorActivity)
    fun inject(templateItemViewerActivity: TemplateItemViewerActivity)
}