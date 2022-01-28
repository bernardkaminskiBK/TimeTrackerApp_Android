package com.berni.timetrackerapp.ui.fragments.statistics

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StatisticsRepository(application)

    val recordMonth = repository.recordMonth.asLiveData()
    val recordYear = repository.recordYear.asLiveData()
    val recordName = repository.recordName.asLiveData()

    fun saveRecordMonth(month: String) = viewModelScope.launch {
        repository.saveRecordMonth(month)
    }

    fun saveRecordYear(year: String) = viewModelScope.launch {
        repository.saveRecordYear(year)
    }

    fun saveRecordName(name: String) = viewModelScope.launch {
        repository.saveRecordName(name)
    }

}