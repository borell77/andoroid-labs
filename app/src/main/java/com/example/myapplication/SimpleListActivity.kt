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

        val dataList = listOf(
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7"
        )

        adapter = SimpleTextAdapter(dataList) { item ->
            val resultIntent = Intent().apply {
                putExtra("SELECTED_ITEM", item)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SimpleListActivity)
            adapter = this@SimpleListActivity.adapter
        }
    }
}