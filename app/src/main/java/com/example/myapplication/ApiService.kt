package com.example.myapplication

import retrofit2.http.GET
import retrofit2.Response

// Модель данных Post
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>
}
