package com.sirionrazzer.diary.system

import com.sirionrazzer.diary.boarding.BoardingActivity
import com.sirionrazzer.diary.boarding.BoardingViewModel
import com.sirionrazzer.diary.history.HistoryActivity
import com.sirionrazzer.diary.history.HistoryViewModel
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.main.MainViewModel
import com.sirionrazzer.diary.trackitem.TrackItemCreatorActivity
import com.sirionrazzer.diary.trackitem.TrackItemCreatorViewModel
import com.sirionrazzer.diary.trackitem.TrackItemViewerActivity
import com.sirionrazzer.diary.trackitem.TrackItemViewerViewModel
import javax.inject.Singleton

@Singleton
interface AppComponent {
    fun inject(boardingActivity: BoardingActivity)
    fun inject(historyActivity: HistoryActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(trackItemCreatorActivity: TrackItemCreatorActivity)
    fun inject(trackItemViewerActivity: TrackItemViewerActivity)

    fun inject(boardingViewModel: BoardingViewModel)
    fun inject(historyViewModel: HistoryViewModel)
    fun inject(mainViewModel: MainViewModel)
    fun inject(trackItemViewerViewModel: TrackItemViewerViewModel)
    fun inject(trackItemCreatorViewModel: TrackItemCreatorViewModel)
}