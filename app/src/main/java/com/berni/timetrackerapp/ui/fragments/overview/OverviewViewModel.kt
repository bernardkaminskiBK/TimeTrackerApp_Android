package com.berni.timetrackerapp.ui.fragments.overview

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = OverviewDetailRepository(application)

    val filterMonth = repository.filterMonth.asLiveData()
    val filterYear = repository.filterYear.asLiveData()

    fun saveFilterMonthToDataStore(month: String) = viewModelScope.launch {
        repository.saveMonthToDataStore(month)
    }

    fun saveFilterYearToDataStore(year: String) = viewModelScope.launch {
        repository.saveYearToDataStore(year)
    }

}