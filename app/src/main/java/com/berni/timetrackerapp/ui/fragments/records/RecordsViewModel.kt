package com.berni.timetrackerapp.ui.fragments.records

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.berni.timetrackerapp.api.data.UnsplashRepository
import com.berni.timetrackerapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordsViewModel @ViewModelInject constructor(private val repository: UnsplashRepository, application: Application)
    : AndroidViewModel(application) {

    private val recordsRepo = RecordsRepository(application)
    val recordTitle = recordsRepo.recordsTitle.asLiveData()

    fun saveRecordTitle(title: String) = viewModelScope.launch {
        recordsRepo.saveTitleRecord(title)
    }

    suspend fun fetchImageUrlBySearchQuery(query: String) =
        withContext(Dispatchers.IO) {
            try {
                val photo = repository.getSearchResult(query)
                if (photo.results.isNotEmpty()) {
                    photo.results[0].urls.regular
                } else {
                    Constants.FAILED_FETCHING
                }
            } catch (e: Exception) {
                Constants.FAILED_FETCHING
            }
        }
}