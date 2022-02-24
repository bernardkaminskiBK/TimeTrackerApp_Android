package com.berni.timetrackerapp.model.database.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.berni.timetrackerapp.model.database.FilterOrder
import com.berni.timetrackerapp.model.database.PreferencesManager
import com.berni.timetrackerapp.model.database.TimeTrackerRepository
import com.berni.timetrackerapp.model.entities.Record
import com.berni.timetrackerapp.utils.Constants
import com.berni.timetrackerapp.utils.Converter.convertTimeToSeconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
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
    val getAllYears = repository.getAllYears().asLiveData()

    fun getTotalTimeRecordsByYear(year: String) =
        repository.getTotalTimeRecordsByYear(year).asLiveData()

    fun getEachRecord() = repository.getEachRecord().asLiveData()

    fun allYearsByName(name: String) = repository.getAllYearByName(name).asLiveData()

    fun saveDataIntoDatabase(imgUrl: String, name: String, time: String) {
        if (imgUrl != Constants.FAILED_FETCHING) {
            saveToDb(name, time, imgUrl)
        } else {
            saveToDb(name, time, imgUrl = "")
        }
    }

    private fun saveToDb(name: String, time: String, imgUrl: String) {
        viewModelScope.launch {
            try {
                val imgResult = repository.getRecordImageUrlByName(name)
                insert(Record(0, System.currentTimeMillis(), name, time.convertTimeToSeconds(), imgResult))
            } catch (e: Exception) {
                insert(Record(0, System.currentTimeMillis(), name, time.convertTimeToSeconds(), imgUrl))
            }
        }
    }


    fun getLastSevenRecordsByNameByYear(name: String, year: String) =
        repository.getLastSevenRecordsByNameByYear(name, year).asLiveData()

    fun getAllRecordsByMonth(month: String) = repository.getAllRecordsByMonth(month).asLiveData()

    fun getMonthsByNameByYear(name: String, year: String) =
        repository.getMonthsByNameByYear(name, year).asLiveData()

    fun getRecordsByNameByDateSumTimeWhereIsSameDate(name: String, date: String) =
        repository.getRecordsByNameByDateSumTimeWhereIsSameDate(name, date).asLiveData()

    fun updateEveryRecordsImgUrlByName(updateImgUrl: String, name: String) = viewModelScope.launch {
        repository.updateEveryRecordsImgUrlByName(updateImgUrl, name)
    }

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

    fun getCountOfRecords() = repository.getCountOfRecords().asLiveData()

    fun getRecordTotalTimeByNameByDate(name: String, date: String) =
        repository.getRecordTotalTimeByNameByDate(name, date).asLiveData()

    fun getRecordTotalDaysByNameByDate(name: String, date: String) =
        repository.getRecordTotalDaysByNameByDate(name, date).asLiveData()

    fun getMostRecentRecordByName(name: String) =
        repository.getMostRecentRecordByName(name).asLiveData()

    fun getLastAddedRecordMonthYearByName(name: String) =
        repository.getLastAddedRecordMonthYearByName(name).asLiveData()

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