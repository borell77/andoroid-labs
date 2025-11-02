package com.example.myapplication.data

import android.content.Context
import com.example.myapplication.DatabaseHelper
import com.example.myapplication.domain.Note

class NotesRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun getAllNotes(): List<Note> {
        return dbHelper.getAllNotes()
    }

    fun saveNote(note: Note): Long {
        return dbHelper.addNote(note)
    }

    // НОВЫЙ МЕТОД
    fun clearAllNotes(): Int {
        return dbHelper.clearAllNotes()
    }
}
