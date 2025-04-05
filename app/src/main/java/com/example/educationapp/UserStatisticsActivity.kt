package com.example.educationapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserStatisticsActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var aboutMeButton: Button

    // Пример полей (будешь заменять данными пользователя из базы или SharedPreferences)
    private lateinit var userIdText: TextView
    private lateinit var userNameText: TextView
    private lateinit var groupText: TextView
    private lateinit var levelsCompletedText: TextView
    private lateinit var currentLevelText: TextView
    private lateinit var currentWorldText: TextView
    private lateinit var rankText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_statistics)

        // Назад
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish() // просто вернуться назад
        }

        // Кнопка "Обо мне"
        aboutMeButton = findViewById(R.id.about_me_button)
        aboutMeButton.setOnClickListener {
            // здесь можно запускать другую активность с подробной инфой
            //val intent = Intent(this, AboutMeActivity::class.java)
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

        // Пример установки значений (здесь можно вставлять из intent или базы)
        userIdText.text = "12345"
        userNameText.text = "Иван Иванов"
        groupText.text = "ПМИ-21"
        levelsCompletedText.text = "10"
        currentLevelText.text = "Уровень 5"
        currentWorldText.text = "Мир 'Математика'"
        rankText.text = "3 из 30"
    }
}
