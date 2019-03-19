package com.sirionrazzer.diary.system

import com.sirionrazzer.diary.boarding.BoardingActivity
import com.sirionrazzer.diary.history.HistoryActivity
import com.sirionrazzer.diary.main.MainActivity
import com.sirionrazzer.diary.trackitem.TrackItemCreatorActivity
import com.sirionrazzer.diary.trackitem.TrackItemViewerActivity
import javax.inject.Singleton

@Singleton
interface AppComponent {
    fun inject(boardingActivity: BoardingActivity)
    fun inject(historyActivity: HistoryActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(trackItemCreatorActivity: TrackItemCreatorActivity)
    fun inject(trackItemViewerActivity: TrackItemViewerActivity)
}