package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.example.myapplication.presentation.MainViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    // --- UI Элементы ---
    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewResult: TextView
    private lateinit var buttonOpenNoteList: Button

    private lateinit var editTextFileContent: EditText
    private lateinit var buttonSaveFile: Button
    private lateinit var buttonLoadFile: Button
    private lateinit var buttonSaveExternal: Button
    private lateinit var buttonLoadExternal: Button

    private lateinit var editTextNoteDb: EditText
    private lateinit var buttonSaveDb: Button
    private lateinit var buttonClearDb: Button

    private lateinit var settingsManager: SettingsManager
    private val internalFileName = "note.txt"

    private val saveFileLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { uri: Uri? ->
        if (uri != null) {
            writeTextToUri(uri)
        } else {
            Toast.makeText(this, "Сохранение файла отменено", Toast.LENGTH_SHORT).show()
        }
    }

    private val openFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            readTextFromUri(uri)
        } else {
            Toast.makeText(this, "Выбор файла отменен", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingsManager = SettingsManager(this)

        initializeViews()
        setupClickListeners()
        observeViewModel()
        loadSavedLogin()
    }

    private fun initializeViews() {
        editTextLogin = findViewById(R.id.editTextLogin)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonOpenNoteList = findViewById(R.id.buttonOpenNoteList)
        textViewResult = findViewById(R.id.textViewResult)
        editTextFileContent = findViewById(R.id.editTextFileContent)
        buttonSaveFile = findViewById(R.id.buttonSaveFile)
        buttonLoadFile = findViewById(R.id.buttonLoadFile)
        buttonSaveExternal = findViewById(R.id.buttonSaveExternal)
        buttonLoadExternal = findViewById(R.id.buttonLoadExternal)
        editTextNoteDb = findViewById(R.id.editTextNoteDb)
        buttonSaveDb = findViewById(R.id.buttonSaveDb)
        buttonClearDb = findViewById(R.id.buttonClearDb)
    }


    private fun setupClickListeners() {
        // --- Логика, не связанная с БД ---
        buttonLogin.setOnClickListener { handleLoginClick() }
        buttonOpenNoteList.setOnClickListener {
            val intent = Intent(this, NoteListActivity::class.java)
            startActivity(intent)
        }
        buttonSaveFile.setOnClickListener { saveTextToInternalFile() }
        buttonLoadFile.setOnClickListener { loadTextFromInternalFile() }
        buttonSaveExternal.setOnClickListener { saveFileLauncher.launch("my_external_note.txt") }
        buttonLoadExternal.setOnClickListener { openFileLauncher.launch("text/plain") }

        buttonSaveDb.setOnClickListener {
            val title = editTextNoteDb.text.toString().trim()
            if (title.isNotEmpty()) {
                mainViewModel.saveNote(title, "Описание для '$title'")
                editTextNoteDb.text.clear()
            } else {
                Toast.makeText(this, "Название заметки не может быть пустым", Toast.LENGTH_SHORT).show()
            }
        }

        buttonClearDb.setOnClickListener {
            mainViewModel.clearAllNotes()
        }
    }


    private fun observeViewModel() {
        // Подписка на список заметок
        mainViewModel.notes.observe(this) { notes ->
            // Этот блок кода выполнится, когда список заметок обновится
            val notesText = if (notes.isEmpty()) {
                "В базе данных пока нет заметок."
            } else {
                "Заметки в БД:\n" + notes.joinToString(separator = "\n") {
                    "ID: ${it.id}, Заголовок: ${it.title}"
                }
            }
            textViewResult.text = notesText
        }

        // Подписка на статус сохранения/очистки
        mainViewModel.operationStatus.observe(this) { status ->
            // Показываем Toast с результатом операции
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadSavedLogin() {
        val savedLogin = settingsManager.getLogin()
        if (savedLogin != null) {
            editTextLogin.setText(savedLogin)
            editTextPassword.requestFocus()
        }
    }

    private fun handleLoginClick() {
        val login = editTextLogin.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Логин и пароль не должны быть пустыми", Toast.LENGTH_SHORT).show()
            return
        }
        settingsManager.saveLogin(login)
        val message = getString(R.string.toast_login_success, login, "*".repeat(password.length))
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        // Обновляем textViewResult, т.к. это не относится к заметкам
        textViewResult.text = "Введённые данные:\nЛогин: $login\nПароль: ${"*".repeat(password.length)}"
    }


    private fun saveTextToInternalFile() {
        openFileOutput(internalFileName, Context.MODE_PRIVATE).use {
            it.write(editTextFileContent.text.toString().toByteArray())
        }
        Toast.makeText(this, "Файл '$internalFileName' сохранен!", Toast.LENGTH_SHORT).show()
        editTextFileContent.text.clear()
    }

    private fun loadTextFromInternalFile() {
        val textFromFile = openFileInput(internalFileName).bufferedReader().useLines { it.joinToString("\n") }
        editTextFileContent.setText(textFromFile)
        Toast.makeText(this, "Файл '$internalFileName' загружен!", Toast.LENGTH_SHORT).show()
    }

    private fun writeTextToUri(uri: Uri) {
        contentResolver.openOutputStream(uri)?.use { stream ->
            OutputStreamWriter(stream).use { it.write(editTextFileContent.text.toString()) }
        }
        Toast.makeText(this, "Файл успешно сохранен во внешнем хранилище!", Toast.LENGTH_SHORT).show()
        editTextFileContent.text.clear()
    }

    private fun readTextFromUri(uri: Uri) {
        val text = contentResolver.openInputStream(uri)?.use { stream ->
            BufferedReader(InputStreamReader(stream)).use { it.readText() }
        }
        editTextFileContent.setText(text)
        Toast.makeText(this, "Файл успешно загружен из внешнего хранилища!", Toast.LENGTH_SHORT).show()
    }
}
