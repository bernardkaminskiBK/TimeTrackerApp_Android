package com.berni.timetrackerapp.ui.fragments.records

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.berni.timetrackerapp.api.ScreenState
import com.berni.timetrackerapp.api.data.UnsplashRepository
import com.berni.timetrackerapp.model.entities.UnsplashPhoto
import kotlinx.coroutines.launch

class RecordsViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository, application: Application)
    : AndroidViewModel(application) {

    private val recordsRepo = RecordsRepository(application)
    val recordTitle = recordsRepo.recordsTitle.asLiveData()

    fun saveRecordTitle(title: String) = viewModelScope.launch {
        recordsRepo.saveTitleRecord(title)
    }

    private val _unsplashApiPhoto = MutableLiveData<ScreenState<List<UnsplashPhoto>?>>()
    val unsplashApiPhoto : LiveData<ScreenState<List<UnsplashPhoto>?>>
        get() = _unsplashApiPhoto

    fun fetchPhotoBySearchQuery(query: String) {
        _unsplashApiPhoto.postValue(ScreenState.Loading(null))
        viewModelScope.launch {
            try {
                val photo = repository.getSearchResult(query)
                _unsplashApiPhoto.postValue(ScreenState.Success(photo.results))
            } catch (e: Exception) {
                _unsplashApiPhoto.postValue(ScreenState.Error(e.message.toString(), null))
            }
        }
    }
}