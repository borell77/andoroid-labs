package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonOpenList: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewForContextMenu: TextView

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

        editTextLogin = findViewById(R.id.editTextLogin)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonOpenList = findViewById(R.id.buttonOpenList)
        textViewResult = findViewById(R.id.textViewResult)
        textViewForContextMenu = findViewById(R.id.textViewForContextMenu)

        // Регистрация контекстного меню
        registerForContextMenu(textViewForContextMenu)

        findViewById<Button>(R.id.buttonOpenFragments).setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            handleLoginClick()
        }

        buttonOpenList.setOnClickListener {
            val login = editTextLogin.text.toString().trim()
            if (login.isEmpty()) {
                Toast.makeText(this, "Введите логин", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, SimpleListActivity::class.java).apply {
                putExtra("SENDER_LOGIN", login)
            }
            openListForResult.launch(intent)
        }

        // Кнопка для AlertDialog
        findViewById<Button>(R.id.btnAlert).setOnClickListener {
            showSimpleDialog()
        }

        // Кнопка для кастомного диалога
        findViewById<Button>(R.id.btnCustomDialog).setOnClickListener {
            showCustomDialog()
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

    // === Options Menu ===
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "Открыты настройки", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_help -> {
                Toast.makeText(this, "Помощь", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // === Context Menu ===
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_copy -> {
                Toast.makeText(this, "Скопировано", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_share -> {
                Toast.makeText(this, "Поделиться", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // === Простой AlertDialog ===
    private fun showSimpleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Подтверждение")
            .setMessage("Вы действительно хотите выйти?")
            .setPositiveButton("Да") { _, _ ->
                Toast.makeText(this, "Выход подтверждён", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    // === Кастомный диалог ===
    private fun showCustomDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)
        val editName = dialogView.findViewById<EditText>(R.id.editName)
        val editEmail = dialogView.findViewById<EditText>(R.id.editEmail)

        AlertDialog.Builder(this)
            .setTitle("Ваши данные")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val name = editName.text.toString().trim()
                val email = editEmail.text.toString().trim()
                if (name.isNotEmpty() && email.isNotEmpty()) {
                    Toast.makeText(this, "Имя: $name, Email: $email", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}