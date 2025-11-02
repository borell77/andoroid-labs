package com.example.myapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun loginButton_updatesResultTextView() {
        val testLogin = "user123"
        val testPassword = "password"

        // Находим поле для логина, очищаем его, вводим текст
        onView(withId(R.id.editTextLogin)).perform(clearText(), typeText(testLogin))

        // Находим поле для пароля, очищаем, вводим текст
        onView(withId(R.id.editTextPassword)).perform(clearText(), typeText(testPassword), closeSoftKeyboard())

        // Находим кнопку входа и нажимаем на нее
        onView(withId(R.id.buttonLogin)).perform(click())

        val expectedText = "Введённые данные:\nЛогин: $testLogin\nПароль: ${"*".repeat(testPassword.length)}"
        onView(withId(R.id.textViewResult)).check(matches(withText(expectedText)))
    }
}
