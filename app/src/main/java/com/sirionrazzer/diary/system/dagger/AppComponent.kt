package com.sirionrazzer.diary.system.dagger

import com.sirionrazzer.diary.boarding.BoardingActivity
import com.sirionrazzer.diary.history.HistoryActivity
import com.sirionrazzer.diary.history.HistoryViewModel
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.main.MainViewModel
import com.sirionrazzer.diary.models.UserStorage
import com.sirionrazzer.diary.trackitem.TemplateItemCreatorActivity
import com.sirionrazzer.diary.trackitem.TemplateItemCreatorViewModel
import com.sirionrazzer.diary.trackitem.TemplateItemViewerActivity
import com.sirionrazzer.diary.trackitem.TemplateItemViewerViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, StorageModule::class])
interface AppComponent {

    fun inject(historyViewModel: HistoryViewModel)
    fun inject(templateItemViewerViewModel: TemplateItemViewerViewModel)
    fun inject(templateItemCreatorViewModel: TemplateItemCreatorViewModel)
    fun inject(mainViewModel: MainViewModel)

    fun inject(mainActivity: MainActivity)
    fun inject(boardingActivity: BoardingActivity)
    fun inject(historyActivity: HistoryActivity)
    fun inject(templateItemCreatorActivity: TemplateItemCreatorActivity)
    fun inject(templateItemViewerActivity: TemplateItemViewerActivity)
}