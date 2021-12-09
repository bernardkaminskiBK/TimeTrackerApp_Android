package com.berni.timetrackerapp.ui.fragments.records

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Records fragment"
    }

    val text: LiveData<String> = _text

}