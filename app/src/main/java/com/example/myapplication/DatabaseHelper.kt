// File: app/src/main/java/com/example/myapplication/DatabaseHelper.kt

package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

// Объект-компаньон для хранения структуры таблицы
object DBContract {
    object NoteEntry : BaseColumns {
        const val TABLE_NAME = "notes"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DESCRIPTION = "description"
    }
}

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // SQL-запрос для создания таблицы
    private val sqlCreateEntries =
        "CREATE TABLE ${DBContract.NoteEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${DBContract.NoteEntry.COLUMN_NAME_TITLE} TEXT," +
                "${DBContract.NoteEntry.COLUMN_NAME_DESCRIPTION} TEXT)"

    // SQL-запрос для удаления таблицы
    private val sqlDeleteEntries = "DROP TABLE IF EXISTS ${DBContract.NoteEntry.TABLE_NAME}"

    // Вызывается при первом создании базы данных
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sqlCreateEntries)
    }

    // Вызывается при обновлении схемы БД
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // удалить старую таблицу и создать новую
        db.execSQL(sqlDeleteEntries)
        onCreate(db)
    }

    // Метод для добавления новой заметки
    fun addNote(title: String, description: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(DBContract.NoteEntry.COLUMN_NAME_TITLE, title)
            put(DBContract.NoteEntry.COLUMN_NAME_DESCRIPTION, description)
        }
        // Вставка новой строки ID новой строки
        val newRowId = db.insert(DBContract.NoteEntry.TABLE_NAME, null, values)
        db.close()
        return newRowId
    }

    // Метод для чтения всех строк
    @SuppressLint("Range")
    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DBContract.NoteEntry.TABLE_NAME}", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                val title = cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMN_NAME_TITLE))
                val description = cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMN_NAME_DESCRIPTION))
                notes.add(Note(id, title, description))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notes
    }

    fun updateNote(id: Long, newTitle: String, newDescription: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(DBContract.NoteEntry.COLUMN_NAME_TITLE, newTitle)
            put(DBContract.NoteEntry.COLUMN_NAME_DESCRIPTION, newDescription)
        }

        // Условие для выбора нужной строки по ID
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        // Выполняем обновление и возвращаем количество измененных строк
        val count = db.update(
            DBContract.NoteEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
        db.close()
        return count
    }
    fun clearAllNotes() {
        val db = this.writableDatabase
        db.delete(DBContract.NoteEntry.TABLE_NAME, null, null)
        db.close()
    }

    // Метод для удаления заметки по ID
    fun deleteNote(id: Long) {
        val db = this.writableDatabase
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        db.delete(DBContract.NoteEntry.TABLE_NAME, selection, selectionArgs)
        db.close()
    }


    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Notes.db"
    }
}
