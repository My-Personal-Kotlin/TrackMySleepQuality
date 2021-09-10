package com.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.trackmysleepquality.database.SleepDatabaseDao

class SleepTrackerViewModel (val database: SleepDatabaseDao, application: Application) : AndroidViewModel(application) {

}
