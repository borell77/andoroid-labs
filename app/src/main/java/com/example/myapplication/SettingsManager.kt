package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(private val context: Context) {


    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveLogin(login: String) {
        // При первом вызове этого метода сработает блок by lazy выше
        sharedPreferences.edit().putString(KEY_LOGIN, login).apply()
    }

    fun getLogin(): String? {
        // Или при первом вызове этого метода
        return sharedPreferences.getString(KEY_LOGIN, null)
    }


    companion object {
        internal const val PREF_NAME = "Settings"

        internal const val KEY_LOGIN = "login"
    }
}
