package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SettingsManagerTest {

    private lateinit var settingsManager: SettingsManager

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private val testLogin = "testUser"

    @Before
    fun setUp() {
        settingsManager = SettingsManager(mockContext)

        `when`(mockContext.getSharedPreferences(SettingsManager.PREF_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putString(SettingsManager.KEY_LOGIN, testLogin)).thenReturn(mockEditor)
    }

    @Test
    fun saveLogin_savesCorrectLogin() {
        settingsManager.saveLogin(testLogin)

        verify(mockEditor).putString(SettingsManager.KEY_LOGIN, testLogin)
        verify(mockEditor).apply()
    }

    @Test
    fun getLogin_returnsSavedLogin() {

        `when`(mockSharedPreferences.getString(SettingsManager.KEY_LOGIN, null)).thenReturn(testLogin)

        val login = settingsManager.getLogin()

        assertEquals(testLogin, login)
    }
}
