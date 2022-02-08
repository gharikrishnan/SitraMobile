package com.sitramobile.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val KEY_SAVED_AT = "MyPrefsSitra"

@SuppressLint("CommitPrefEdits")
class SessionManager(private var ctx: Context) {
    var prefs: SharedPreferences
    var editor: SharedPreferences.Editor

    private val appContext = ctx.applicationContext

    private val preferences : SharedPreferences

        get() = PreferenceManager.getDefaultSharedPreferences(appContext)
    init {
        prefs = appContext.getSharedPreferences(KEY_SAVED_AT, Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    fun logout(): Boolean {
        editor.clear()
        return prefs.getBoolean("logtrue", false)
    }

}
