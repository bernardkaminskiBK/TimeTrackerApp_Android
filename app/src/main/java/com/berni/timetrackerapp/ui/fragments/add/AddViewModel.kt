package com.berni.timetrackerapp.ui.fragments.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is ADD fragment"
    }

    val text: LiveData<String> = _text

}