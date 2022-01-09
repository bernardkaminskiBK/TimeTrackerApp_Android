package com.berni.timetrackerapp.ui.fragments.statistics

import android.app.Application
import androidx.lifecycle.*
import com.berni.timetrackerapp.model.database.FilterOrder
import kotlinx.coroutines.launch

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StatisticsRepository(application)

    val recordName = repository.recordName.asLiveData()
    val recordDate = repository.recordDate.asLiveData()

    fun saveNameOfRecord(name: String) = viewModelScope.launch {
        repository.saveNameOfRecord(name)
    }

    fun saveDateOfRecord(date: String) = viewModelScope.launch {
        repository.savaDateOfRecord(date)
    }

}