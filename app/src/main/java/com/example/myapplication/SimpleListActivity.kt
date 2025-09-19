package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

class SimpleListActivity : AppCompatActivity() {

    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var adapter: SimpleTextAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_list)

        recyclerView = findViewById(R.id.recyclerView)

        // Простой список строк — тестовые данные
        val dataList = listOf(
            "Элемент 1",
            "Элемент 2",
            "Элемент 3",
            "Элемент 4",
            "Элемент 5",
            "Элемент 6",
            "Элемент 7",
            "Элемент 8",
            "Элемент 9",
            "Элемент 10",
            "Элемент 11",
            "Элемент 12",
            "Элемент 13",
            "Элемент 14",
            "Элемент 15",
            "Элемент 9",
            "Элемент 10",
            "Элемент 11",
            "Элемент 12",
            "Элемент 13",
            "Элемент 14"
        )

        // Создаем адаптер
        adapter = SimpleTextAdapter(dataList) { item ->
            Toast.makeText(this, "Выбрано: $item", Toast.LENGTH_SHORT).show()
        }

        // Настраиваем RecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SimpleListActivity)
            adapter = this@SimpleListActivity.adapter
        }
    }
}