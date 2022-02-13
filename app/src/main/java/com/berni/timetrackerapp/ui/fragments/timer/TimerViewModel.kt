package com.berni.timetrackerapp.ui.fragments.timer

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.berni.timetrackerapp.api.ScreenState
import com.berni.timetrackerapp.api.data.UnsplashRepository
import com.berni.timetrackerapp.model.entities.UnsplashPhoto
import com.berni.timetrackerapp.service.TimerService
import com.berni.timetrackerapp.utils.Formatter
import kotlinx.coroutines.launch

class TimerViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository, application: Application
) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private var serviceIntent: Intent = Intent(context, TimerService::class.java)

    private val _actualTime = MutableLiveData<String>()
    private val _timerStarted = MutableLiveData(false)

    var actualTime: LiveData<String> = _actualTime
    var timerStarted: LiveData<Boolean> = _timerStarted

    private var time = 0.0

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

    fun startTimer() {
        context.registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        context.startService(serviceIntent)
        _timerStarted.value = true
    }

    fun stopTimer() {
        context.stopService(serviceIntent)
        _timerStarted.value = false
    }

    fun resetTimer() {
        stopTimer()
        time = 0.0
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            _actualTime.value = Formatter.getTimeStringFromDouble(time)
        }
    }

}