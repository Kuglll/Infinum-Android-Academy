package com.example.kuglll.shows_mark.utils

import android.app.Application

class AppInstance : Application(){
    companion object {
        lateinit var instance: AppInstance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}