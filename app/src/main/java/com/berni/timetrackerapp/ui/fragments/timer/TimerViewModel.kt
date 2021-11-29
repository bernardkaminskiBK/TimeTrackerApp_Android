package com.berni.timetrackerapp.ui.fragments.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Timer fragment"
    }

    val text: LiveData<String> = _text
}