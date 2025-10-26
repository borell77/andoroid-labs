package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class PostsAdapter(
    private var posts: List<Post>,
    private val onItemClick: (Post) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    // ViewHolder для отображения данных поста
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(android.R.id.text1)
        private val bodyTextView: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(post: Post, onItemClick: (Post) -> Unit) {
            titleTextView.text = "Title: ${post.title}"
            bodyTextView.text = post.body
            itemView.setOnClickListener { onItemClick(post) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        // стандартный layout который содержит два TextView (text1 и text2)
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], onItemClick)
    }

    override fun getItemCount() = posts.size

    // Метод для обновления данных в адаптере
    fun updateData(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged() // Сообщаем RecyclerView, что данные изменились
    }
}
