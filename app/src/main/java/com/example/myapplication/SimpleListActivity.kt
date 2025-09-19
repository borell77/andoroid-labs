package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.TextView

class SimpleListActivity : AppCompatActivity() {

    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var adapter: SimpleTextAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_list)

        // 👇 Получаем данные, переданные из MainActivity
        val senderLogin = intent.getStringExtra("SENDER_LOGIN") ?: "Гость"
        val titleText = "Список пользователей (от: $senderLogin)"

        // Обновляем заголовок
        val titleView = findViewById<TextView>(R.id.textViewTitle)
        titleView.text = titleText

        // Пример данных — можно сделать динамическим
        val dataList = listOf(
            "$senderLogin — Админ",
            "Александр",
            "Мария",
            "Иван",
            "Екатерина",
            "Дмитрий",
            "Ольга",
            "Павел",
            "Наталья",
            "Сергей",
            "Татьяна"
        )

        adapter = SimpleTextAdapter(dataList) { item ->
            // 👇 Возвращаем результат обратно в MainActivity
            val resultIntent = Intent().apply {
                putExtra("SELECTED_ITEM", item)
            }
            setResult(RESULT_OK, resultIntent)
            finish() // Закрываем этот экран
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SimpleListActivity)
            adapter = this@SimpleListActivity.adapter
        }
    }
}