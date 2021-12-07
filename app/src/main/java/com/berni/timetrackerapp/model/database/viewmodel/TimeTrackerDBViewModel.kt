package com.berni.timetrackerapp.model.database.viewmodel

import androidx.lifecycle.*
import com.berni.timetrackerapp.model.database.TimeTrackerRepository
import com.berni.timetrackerapp.model.entities.Progress
import kotlinx.coroutines.launch

class TimeTrackerDBViewModel(private val repository: TimeTrackerRepository): ViewModel() {

    val allProgressList: LiveData<List<Progress>> =
        repository.allTimeTrackerProgressList.asLiveData()

    val allProgressNames: LiveData<List<String>> =
        repository.allTimeTrackerProgressNames.asLiveData()

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

    fun getFilteredProgressList(value : String) : LiveData<List<Progress>> =
        repository.getFilteredTimeTrackerProgresses(value).asLiveData()

}

class TimeTrackerViewModelFactory(private val repository: TimeTrackerRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TimeTrackerDBViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimeTrackerDBViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}