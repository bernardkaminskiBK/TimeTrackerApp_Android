package com.berni.timetrackerapp.ui.fragments.records

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecordsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecordsRepository(application)

    val recordTitle = repository.recordsTitle.asLiveData()

    fun saveRecordTitle(title: String) = viewModelScope.launch {
        repository.saveTitleRecord(title)
    }

}