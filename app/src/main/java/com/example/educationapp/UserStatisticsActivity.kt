package com.example.educationapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserStatisticsActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var aboutMeButton: Button
    private lateinit var prefs: SharedPreferences
    // Пример полей (будешь заменять данными пользователя из базы или SharedPreferences)
    private lateinit var userIdText: TextView
    private lateinit var userNameText: TextView
    private lateinit var groupText: TextView
    private lateinit var levelsCompletedText: TextView
    private lateinit var currentLevelText: TextView
    private lateinit var currentWorldText: TextView
    private lateinit var rankText: TextView
    private val prefs2 by lazy { getSharedPreferences("LevelProgress", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_statistics)
        prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        // Назад
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish() // просто вернуться назад
        }

        // Кнопка "Обо мне"
        aboutMeButton = findViewById(R.id.about_me_button)
        aboutMeButton.setOnClickListener {
            val intent = Intent(this, AboutMeActivity::class.java)
            startActivity(intent)
        }

        // Привязка текста (пример для подстановки данных)
        userIdText = findViewById(R.id.text_id)
        userNameText = findViewById(R.id.text_name)
        groupText = findViewById(R.id.text_group)
        levelsCompletedText = findViewById(R.id.text_levels)
        currentLevelText = findViewById(R.id.text_current_level)
        currentWorldText = findViewById(R.id.text_current_world)
        rankText = findViewById(R.id.text_rank)

        // Загрузка данных пользователя
        loadUserData()
    }

    private fun loadUserData() {
        val lastName = prefs.getString("last_name", "") ?: ""
        val firstName = prefs.getString("first_name", "") ?: ""
        val middleName = prefs.getString("middle_name", "") ?: ""
        val group = prefs.getString("group", "") ?: ""
        val levelsCompleted = prefs2.getInt("levels_completed", 0)
        val currentLevel = prefs2.getInt("current_level", 1)
        val rankPosition = prefs.getInt("user_rank_position", 0)
        userIdText.text = "ID: 5"
        userNameText.text = "$lastName $firstName $middleName".trim()
        groupText.text = "Группа: " + group
        levelsCompletedText.text = "Завершенных уровней: " + levelsCompleted.toString()
        currentLevelText.text = "Текущий уровень ${currentLevel}"
        currentWorldText.text = "Текущий мир дисциплины: " + prefs.getString("current_world", "Программирование на C#")
        rankText.text = "Место в рейтинге: " + rankPosition.toString()
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

}
