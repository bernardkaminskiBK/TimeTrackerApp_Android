package com.berni.timetrackerapp.ui.fragments.timer

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.berni.timetrackerapp.utils.Formatter
import com.berni.timetrackerapp.utils.TimerService

class TimerViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private var serviceIntent: Intent = Intent(context, TimerService::class.java)

    private val _actualTime = MutableLiveData<String>()
    private val _timerStarted = MutableLiveData<Boolean>().apply {
        value = false
    }

    var actualTime: LiveData<String> = _actualTime
    var timerStarted: LiveData<Boolean> = _timerStarted

    private var time = 0.0

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
            println(time)
            _actualTime.value = Formatter.getTimeStringFromDouble(time)
        }
    }

}