package com.example.myapplication.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.NotesRepository
import com.example.myapplication.domain.Note

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val notesRepository = NotesRepository(application.applicationContext)

    // LiveData для списка заметок
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    // LiveData для статуса операции
    private val _operationStatus = MutableLiveData<String>()
    val operationStatus: LiveData<String> = _operationStatus

    init {
        loadNotes()
    }

    fun loadNotes() {
        _notes.value = notesRepository.getAllNotes()
    }

    fun saveNote(title: String, description: String) {
        val newNote = Note(title = title, description = description)
        val newRowId = notesRepository.saveNote(newNote)

        if (newRowId > -1) {
            _operationStatus.value = "Заметка '$title' успешно сохранена!"
            loadNotes()
        } else {
            _operationStatus.value = "Ошибка сохранения заметки."
        }
    }

    fun clearAllNotes() {
        val clearedRows = notesRepository.clearAllNotes()
        if (clearedRows > 0) {
            _operationStatus.value = "Все заметки ($clearedRows шт.) удалены."
            loadNotes()
        } else {
            _operationStatus.value = "Нет заметок для удаления."
        }
    }
}
