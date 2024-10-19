package com.example.homeinventoryapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HomeInventoryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //timber to be initialized here
    }
}