package com.berni.timetrackerapp.model.database.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.berni.timetrackerapp.model.database.FilterOrder
import com.berni.timetrackerapp.model.database.PreferencesManager
import com.berni.timetrackerapp.model.database.TimeTrackerRepository
import com.berni.timetrackerapp.model.entities.Record
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DatabaseViewModel(application: Application, private val repository: TimeTrackerRepository) :
    AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val preferencesManager = PreferencesManager(context)
    private val preferencesFlow = preferencesManager.preferencesFlow

    fun onFilterOrderSelected(filterOrder: FilterOrder) = viewModelScope.launch {
        preferencesManager.updateFilterOrder(filterOrder)
    }

    fun onQuerySelected(filterOrder: String) = viewModelScope.launch {
        preferencesManager.updateFilterQuery(filterOrder)
    }

    @ExperimentalCoroutinesApi
    private val recordsFlow = preferencesFlow
        .flatMapLatest { (filterQuery, filterPreferences) ->
            repository.getRecordsList(filterQuery, filterPreferences)
        }

    @ExperimentalCoroutinesApi
    val records = recordsFlow.asLiveData()
    val allRecordNames = repository.getAllRecordsName.asLiveData()
    val getTotalTimeRecords =  repository.getTotalTimeRecords().asLiveData()
    val getAllMonths = repository.getAllMonths().asLiveData()

    fun getAllRecordsByMonth(month: String) = repository.getAllRecordsByMonth(month).asLiveData()
    fun getAllRecordsDateByName(name: String) = repository.getAllRecordsDateByName(name).asLiveData()
    fun getRecordsByNameAndDate(name: String, date: String) = repository.getRecordsByNameAndDate(name, date).asLiveData()

    fun insert(record: Record) = viewModelScope.launch {
        repository.insertRecord(record)
    }

    fun update(record: Record) = viewModelScope.launch {
        repository.updateRecord(record)
    }

    fun delete(record: Record) = viewModelScope.launch {
        repository.deleteRecord(record)
    }

    fun deleteAllRecords() = viewModelScope.launch {
        repository.deleteAllRecords()
    }

}

class TimeTrackerViewModelFactory(
    private val application: Application,
    private val repository: TimeTrackerRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DatabaseViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}