package com.example.kuglll.shows_mark.dataClasses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel(){

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val episodeInserted = MutableLiveData<Boolean>()


}