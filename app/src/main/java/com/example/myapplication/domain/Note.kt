package com.example.myapplication.domain

// data-класс для хранения информации о заметке
data class Note(
    val id: Long = 0L,
    val title: String,
    val description: String
)