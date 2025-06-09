package com.example.educationapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Path
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class BadEndActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private val prefs by lazy { getSharedPreferences("game_prefs", MODE_PRIVATE) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.bad_end_cat_was_beat)
        // Сбрасываем флаг рыбки при каждом поражении
        prefs.edit { putBoolean("fish_recovered", false) }
        mediaPlayer = MediaPlayer.create(this, R.raw.cat_tapok)
        mediaPlayer.start()

        Handler(Looper.getMainLooper()).postDelayed({
            setContentView(R.layout.bad_end_cat_dead)

            val catSpirit: ImageView = findViewById(R.id.cat_spirit)
            val youDiedText: TextView = findViewById(R.id.you_died_text)

            catSpirit.post {
                val startX = catSpirit.x
                val startY = catSpirit.y

                val path = Path().apply {
                    moveTo(startX, startY)
                    val amplitude = 100f
                    val stepY = -50f
                    for (i in 1..10) {
                        val dx = if (i % 2 == 0) -amplitude else amplitude
                        rLineTo(dx, stepY)
                    }
                }

                val zigzag = ObjectAnimator.ofFloat(catSpirit, View.X, View.Y, path).apply {
                    duration = 3000
                    interpolator = LinearInterpolator()
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            youDiedText.visibility = View.VISIBLE
                            youDiedText.alpha = 0f
                            youDiedText.animate()
                                .alpha(1f)
                                .setDuration(1000)
                                .start()
                        }
                        override fun onAnimationEnd(animation: Animator) {
                            setContentView(R.layout.bad_end_message)
                            findViewById<Button>(R.id.exit_button).setOnClickListener {
                                checkFishRecovery() // Проверяем, нужно ли показывать рыбку
                            }
                        }
                    })
                }
                zigzag.start()
            }

            Handler(Looper.getMainLooper()).postDelayed({
                setContentView(R.layout.bad_end_message)
                findViewById<Button>(R.id.exit_button).setOnClickListener {
                    finish()
                }
            }, 4000)
        }, 2000)
    }
    private fun checkFishRecovery() {
        if (!prefs.getBoolean("fish_recovered", false)) {
            // Если рыбка не восстановлена, переходим на экран восстановления
            startActivity(Intent(this, RecoverFishActivity::class.java))
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
