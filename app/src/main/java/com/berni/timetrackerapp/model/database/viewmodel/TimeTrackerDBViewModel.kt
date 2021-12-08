package com.berni.timetrackerapp.model.database.viewmodel

import androidx.lifecycle.*
import com.berni.timetrackerapp.model.database.TimeTrackerRepository
import com.berni.timetrackerapp.model.entities.Progress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

enum class FilterOrder { BY_NAME, SHOW_ALL }

class TimeTrackerDBViewModel(private val repository: TimeTrackerRepository) : ViewModel() {

    val filterQuery = MutableStateFlow("")
    val filterOrder = MutableStateFlow(FilterOrder.SHOW_ALL)

    @ExperimentalCoroutinesApi
    private val progressesFlow = combine(
        filterQuery, filterOrder
    ) { filterQuery, filterOrder ->
        Pair(filterQuery, filterOrder)
    }.flatMapLatest { (filterQuery, filterOrder) ->
        repository.getProgressesList(filterQuery, filterOrder)
    }

    @ExperimentalCoroutinesApi
    val progresses = progressesFlow.asLiveData()

    val allProgressNames = repository.allTimeTrackerProgressNames.asLiveData()

    fun insert(progress: Progress) = viewModelScope.launch {
        repository.insertTimerTrackerProgressData(progress)
    }

    fun update(progress: Progress) = viewModelScope.launch {
        repository.updateTimeTrackerProgressData(progress)
    }

    fun delete(progress: Progress) = viewModelScope.launch {
        repository.deleteTimeTrackerProgressData(progress)
    }

    fun deleteAllProgressRecords() = viewModelScope.launch {
        repository.deleteAllProgressRecords()
    }

}

class TimeTrackerViewModelFactory(private val repository: TimeTrackerRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimeTrackerDBViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimeTrackerDBViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}