package com.berni.timetrackerapp.model.database.viewmodel

import androidx.lifecycle.*
import com.berni.timetrackerapp.model.database.TimeTrackerRepository
import com.berni.timetrackerapp.model.entities.Record
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

enum class FilterOrder { BY_NAME, SHOW_ALL }

class DatabaseViewModel(private val repository: TimeTrackerRepository) : ViewModel() {

    val filterQuery = MutableStateFlow("")
    val filterOrder = MutableStateFlow(FilterOrder.SHOW_ALL)

    @ExperimentalCoroutinesApi
    private val recordsFlow = combine(
        filterQuery, filterOrder
    ) { filterQuery, filterOrder ->
        Pair(filterQuery, filterOrder)
    }.flatMapLatest { (filterQuery, filterOrder) ->
        repository.getRecordsList(filterQuery, filterOrder)
    }

    @ExperimentalCoroutinesApi
    val records = recordsFlow.asLiveData()

    val allRecordNames = repository.getAllRecordsName.asLiveData()

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

class TimeTrackerViewModelFactory(private val repository: TimeTrackerRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DatabaseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}