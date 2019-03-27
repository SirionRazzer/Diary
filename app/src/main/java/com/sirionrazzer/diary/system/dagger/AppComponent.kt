package com.sirionrazzer.diary.system.dagger

import com.sirionrazzer.diary.boarding.BoardingActivity
import com.sirionrazzer.diary.history.HistoryActivity
import com.sirionrazzer.diary.history.HistoryViewModel
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.main.MainViewModel
import com.sirionrazzer.diary.trackitem.TrackItemCreatorActivity
import com.sirionrazzer.diary.trackitem.TrackItemCreatorViewModel
import com.sirionrazzer.diary.trackitem.TrackItemViewerActivity
import com.sirionrazzer.diary.trackitem.TrackItemViewerViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, StorageModule::class])
interface AppComponent {
    fun inject(historyViewModel: HistoryViewModel)
    fun inject(trackItemViewerViewModel: TrackItemViewerViewModel)
    fun inject(trackItemCreatorViewModel: TrackItemCreatorViewModel)
    fun inject(mainViewModel: MainViewModel)

    fun inject(boardingActivity: BoardingActivity)
    fun inject(historyActivity: HistoryActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(trackItemCreatorActivity: TrackItemCreatorActivity)
    fun inject(trackItemViewerActivity: TrackItemViewerActivity)
}