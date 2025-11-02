package com.example.myapplication

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.domain.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoteListActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewNotes)
        val fab: FloatingActionButton = findViewById(R.id.fabAddNote)

        setupRecyclerView()
        loadNotes()

        fab.setOnClickListener {
            showAddNoteDialog()
        }
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(
            onItemClicked = { note ->
                showEditNoteDialog(note)
            },
            onDeleteClicked = { note ->
                showDeleteConfirmationDialog(note)
            }
        )
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadNotes() {
        val notes = dbHelper.getAllNotes()
        noteAdapter.submitList(notes)
    }


    private fun showEditNoteDialog(note: Note) {
        val editText = EditText(this).apply {
            setText(note.title)
        }

        AlertDialog.Builder(this)
            .setTitle("Редактировать заметку")
            .setView(editText)
            .setPositiveButton("Сохранить") { dialog, _ ->
                val newTitle = editText.text.toString().trim()
                if (newTitle.isNotEmpty()) {
                    val newDescription = "Обновленное описание для '$newTitle'"
                    dbHelper.updateNote(note.id, newTitle, newDescription)
                    loadNotes()
                    Toast.makeText(this, "Заметка обновлена", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .create()
            .show()
    }

    private fun showAddNoteDialog() {
        val editText = EditText(this).apply {
            hint = "Введите название заметки"
        }

        AlertDialog.Builder(this)
            .setTitle("Новая заметка")
            .setView(editText)
            .setPositiveButton("Добавить") { dialog, _ ->
                val title = editText.text.toString().trim()
                if (title.isNotEmpty()) {

                    val newNote = Note(title = title, description = "Описание для '$title'")

                    dbHelper.addNote(newNote)

                    loadNotes()
                    Toast.makeText(this, "Заметка добавлена", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .create()
            .show()
    }

    private fun showDeleteConfirmationDialog(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Удалить заметку?")
            .setMessage("Вы уверены, что хотите удалить '${note.title}'?")
            .setPositiveButton("Да") { _, _ ->
                dbHelper.deleteNote(note.id)
                loadNotes()
                Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Нет", null)
            .create()
            .show()
    }
}
