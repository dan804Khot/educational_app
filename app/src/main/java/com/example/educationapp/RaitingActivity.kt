package com.example.educationapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class RatingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val backButton: ImageButton = findViewById(R.id.back_button)
        val ratingList: ListView = findViewById(R.id.rating_list)
        val updateButton: Button = findViewById(R.id.update_date_button)

        // Данные для списка рейтинга
        val users = listOf(
            "1  Имя пользователя: Иван",
            "2  Имя пользователя: Алексей",
            "3  Имя пользователя: Ольга",
            "4  Имя пользователя: Мария",
            "5  Имя пользователя: Дмитрий",
            "6  Имя пользователя: Анна"
        )

        // Адаптер для ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, users)
        ratingList.adapter = adapter

        // Кнопка назад
        backButton.setOnClickListener {
            finish()
        }

        // Кнопка обновления даты
        updateButton.setOnClickListener {
            updateButton.text = "Дата обновления: " + java.util.Calendar.getInstance().time.toString()
        }
    }
}
