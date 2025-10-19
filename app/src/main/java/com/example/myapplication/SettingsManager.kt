package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLogin(login: String) {
        prefs.edit().putString(KEY_LOGIN, login).apply()
    }

    fun getLogin(): String? {
        return prefs.getString(KEY_LOGIN, null)
    }

    // Компаньон для хранения констант
    companion object {
        private const val PREFS_NAME = "MyApplicationPrefs"
        private const val KEY_LOGIN = "user_login"
    }
}
