package com.example.swudolistapp

import android.app.Application

class App: Application() {
    companion object {
        lateinit var prefs : SharedManager
    }
    /* prefs라는 이름의 MySharedPreferences 하나만 생성할 수 있도록 설정. */

    override fun onCreate() {
        prefs = SharedManager(applicationContext)
        super.onCreate()
    }
}