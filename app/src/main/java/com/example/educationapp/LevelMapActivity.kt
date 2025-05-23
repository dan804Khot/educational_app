package com.example.educationapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class LevelMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_map_activity)

        // Кнопка Назад
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            finish()
        }

        // Кнопки уровней
        findViewById<Button>(R.id.level1).setOnClickListener {
            Toast.makeText(this, "Уровень 1", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, Level1Activity::class.java))
        }

        findViewById<Button>(R.id.level2).setOnClickListener {
            Toast.makeText(this, "Уровень 2", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.level3).setOnClickListener {
            Toast.makeText(this, "Уровень 3", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.level4).setOnClickListener {
            Toast.makeText(this, "Уровень 4", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.level5).setOnClickListener {
            Toast.makeText(this, "Уровень 5", Toast.LENGTH_SHORT).show()
        }

        // Рейтинг
        findViewById<Button>(R.id.rating_button).setOnClickListener {
            Toast.makeText(this, "Переход к рейтингу", Toast.LENGTH_SHORT).show()
        }

        // Прогресс
        findViewById<Button>(R.id.progress_button).setOnClickListener {
            Toast.makeText(this, "Переход к прогрессу", Toast.LENGTH_SHORT).show()
        }
    }
}
