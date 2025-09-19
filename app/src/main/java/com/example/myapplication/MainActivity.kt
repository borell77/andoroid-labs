package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Объявляем переменные для элементов интерфейса
    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonOpenList: Button
    private lateinit var textViewResult: TextView

    // 🚨 ЛАУНЧЕР ДЛЯ ОЖИДАНИЯ РЕЗУЛЬТАТА ИЗ SimpleListActivity
    private val openListForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedName = result.data?.getStringExtra("SELECTED_ITEM")
            if (selectedName != null) {
                textViewResult.text = "Выбрано из списка: $selectedName"
                Toast.makeText(this, "Выбрано: $selectedName", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Lifecycle", "onCreate: Activity создана")

        // Инициализация элементов UI
        editTextLogin = findViewById(R.id.editTextLogin)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonOpenList = findViewById(R.id.buttonOpenList)
        textViewResult = findViewById(R.id.textViewResult)

        // Обработчик кнопки "Войти"
        buttonLogin.setOnClickListener {
            handleLoginClick()
        }

        // 🚨 Обработчик кнопки "Открыть список"
        buttonOpenList.setOnClickListener {
            val login = editTextLogin.text.toString().trim()
            if (login.isEmpty()) {
                Toast.makeText(this, "Введите логин, чтобы передать его в список", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 👇 Передаём данные в SimpleListActivity через Intent
            val intent = Intent(this, SimpleListActivity::class.java).apply {
                putExtra("SENDER_LOGIN", login) // Передаём логин как данные
            }
            openListForResult.launch(intent) // Запускаем и ждём результат
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

        val message = getString(R.string.toast_login_success, login, "*".repeat(password.length))
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        textViewResult.text = "Введённые данные:\nЛогин: $login\nПароль: ${"*".repeat(password.length)}"
    }

    // ЖИЗНЕННЫЙ ЦИКЛ — оставляем как есть
    override fun onStart() { super.onStart(); Log.d("Lifecycle", "onStart: Activity видима") }
    override fun onResume() { super.onResume(); Log.d("Lifecycle", "onResume: Activity активна") }
    override fun onPause() { super.onPause(); Log.d("Lifecycle", "onPause: Activity частично скрыта") }
    override fun onStop() { super.onStop(); Log.d("Lifecycle", "onStop: Activity полностью скрыта") }
    override fun onDestroy() { super.onDestroy(); Log.d("Lifecycle", "onDestroy: Activity уничтожена") }
    override fun onRestart() { super.onRestart(); Log.d("Lifecycle", "onRestart: Activity перезапущена") }
}