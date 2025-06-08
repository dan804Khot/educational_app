package com.example.educationapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RatingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RatingAdapter
    private lateinit var back_button: ImageButton
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        recyclerView = findViewById(R.id.rating_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        back_button = findViewById(R.id.back_button)

        back_button.setOnClickListener {
            finish()
        }
        loadRatingData()
    }

    private fun loadRatingData() {
        // Получаем данные текущего пользователя

        val lastName = prefs.getString("last_name", "") ?: ""
        val firstName = prefs.getString("first_name", "") ?: ""

        val currentUser = RatingItem(
            username = "$lastName $firstName",  // Используем сохраненные имя и фамилию
            score = prefs.getInt("user_score", 0),
            isCurrentUser = true
        )

        // Создаем список пользователей
        val users = mutableListOf(
            RatingItem("Гусев Дмитрий", 1200),
            RatingItem("Чудакова Екатерина", 1100),
            currentUser, // Добавляем текущего пользователя
            RatingItem("Иванов Иван", 30),
            RatingItem("Петрова Анна", 20)
        )

        // Сортируем по убыванию очков
        users.sortByDescending { it.score }

        // Находим позицию текущего пользователя
        val userPosition = users.indexOfFirst { it.isCurrentUser } + 1

        prefs.edit {
            putInt("user_rank_position", userPosition)
            apply()
        }

        adapter = RatingAdapter(users)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
