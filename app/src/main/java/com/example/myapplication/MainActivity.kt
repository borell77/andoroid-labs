package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Объявляем переменные для элементов интерфейса
    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Lifecycle", "onCreate: Activity создана")

        // Инициализация элементов UI
        editTextLogin = findViewById(R.id.editTextLogin)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewResult = findViewById(R.id.textViewResult)

        // Устанавливаем обработчик нажатия на кнопку
        buttonLogin.setOnClickListener {
            handleLoginClick()
        }
    }

    private fun handleLoginClick() {
        val login = editTextLogin.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (login.isEmpty()) {
            Toast.makeText(this, R.string.toast_error_login, Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, R.string.toast_error_password, Toast.LENGTH_SHORT).show()
            return
        }

        // Используем форматированную строку
        val message = getString(R.string.toast_login_success, login, "*".repeat(password.length))
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        // Обновляем TextView
        val resultText = getString(R.string.result_text)
        textViewResult.text = "Введённые данные:\nЛогин: $login\nПароль: ${"*".repeat(password.length)}"
    }

    // ЖИЗНЕННЫЙ ЦИКЛ ACTIVITY — добавляем логирование всех методов
    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "onStart: Activity видима")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "onResume: Activity активна и взаимодействует с пользователем")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "onPause: Activity частично скрыта")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "onStop: Activity полностью скрыта")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "onDestroy: Activity уничтожена")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("Lifecycle", "onRestart: Activity перезапущена после onStop")
    }
}