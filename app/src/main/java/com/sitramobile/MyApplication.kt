package com.sitramobile


import android.app.Application
import android.content.Context
import com.sitramobile.db.AppDatabase


class MyApplication : Application() {

    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        AppDatabase.invoke(context)

    }


}