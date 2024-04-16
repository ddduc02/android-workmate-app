package com.example.mychatapp

import android.app.Application
import android.util.Log
import com.example.mychatapp.util.SharedPreferencesUtil


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        lateinit var application: Application
            private set

        var myUserID: String = ""
            get() {
                if (field.isEmpty()) {
                    field = SharedPreferencesUtil.getUserID(application.applicationContext).orEmpty()
                    Log.d("Check", field)
                }
                return field
            }
            private set
    }
}