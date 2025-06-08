package com.example.educationapp

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class GoodEndActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.succsesfull_completed)

        mediaPlayer = MediaPlayer.create(this, R.raw.cat_hand)
        mediaPlayer.start()
        // Задержка перед показом сообщения о завершении
        runnable = Runnable {
            showCompletionMessage()
        }
        handler.postDelayed(runnable, 4000)
    }

    private fun showCompletionMessage() {
        setContentView(R.layout.activity_succsesfull_completed_message)
        findViewById<Button>(R.id.exit_button).setOnClickListener {
            returnToMap()
        }
    }

    private fun returnToMap() {
        val prefs = getSharedPreferences("LevelProgress", Context.MODE_PRIVATE)
        val currentLevel = intent.getIntExtra("level", 1)
        prefs.edit {
            putInt("current_level", currentLevel + 1)
        }

        // Возвращаемся на карту с обновленными данными
        val intent = Intent(this, LevelMapActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        mediaPlayer.release()
        super.onDestroy()
    }
}