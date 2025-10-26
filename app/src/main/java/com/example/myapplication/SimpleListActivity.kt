package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // Импорт для lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch // Импорт для launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SimpleListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter
    private lateinit var apiService: ApiService
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_list)

        // Инициализация View
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Инициализация адаптера
        adapter = PostsAdapter(emptyList()) { post ->
            val resultIntent = Intent().apply {
                putExtra("SELECTED_ITEM", post.title)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        recyclerView.adapter = adapter

        // Настройка Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
        fetchPosts()
    }

    private fun fetchPosts() {
        // Показываем ProgressBar и скрываем список
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                // Retrofit сам выполнит запрос в фоновом потоке
                val response = apiService.getPosts()

                if (response.isSuccessful) {
                    val posts = response.body()
                    if (posts != null) {
                        adapter.updateData(posts)
                    }
                }
            } catch (e: Exception) {
                // Обработка ошибок сети или других исключений
                Log.e("SimpleListActivity", "Ошибка при загрузке: ${e.message}", e)
                Toast.makeText(
                    this@SimpleListActivity,
                    "Ошибка сети: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }
    }
}
