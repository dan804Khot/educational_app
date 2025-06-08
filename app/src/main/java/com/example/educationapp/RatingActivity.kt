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
    private val prefs by lazy {
        getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }
    private val prefs2 by lazy {
        getSharedPreferences("LevelProgress", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        recyclerView = findViewById(R.id.rating_list)
        back_button = findViewById(R.id.back_button)

        recyclerView.layoutManager = LinearLayoutManager(this)
        back_button.setOnClickListener { finish() }

        loadRatingData()
    }

    private fun loadRatingData() {
        // Получаем данные текущего пользователя
        val lastName = prefs.getString("last_name", "") ?: ""
        val firstName = prefs.getString("first_name", "") ?: ""
        val userScore = prefs2.getInt("user_score", 0)

        val currentUser = RatingItem(
            username = "$lastName $firstName",
            score = userScore,
            isCurrentUser = true
        )

        val users = mutableListOf(
            RatingItem("Гусев Дмитрий", 1200),
            RatingItem("Чудакова Екатерина", 1100),
            RatingItem("Иванов Иван", 30),
            RatingItem("Петрова Анна", 20),
            currentUser // Добавляем текущего пользователя
        )

        // Сортируем по убыванию очков
        users.sortByDescending { it.score }

        // Находим позицию текущего пользователя
        val userPosition = users.indexOfFirst { it.isCurrentUser } + 1

        // Сохраняем позицию
        prefs.edit {
            putInt("user_rank_position", userPosition)
            apply()
        }

        adapter = RatingAdapter(users)
        recyclerView.adapter = adapter

    }
}
