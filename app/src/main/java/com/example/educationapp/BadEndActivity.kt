package com.example.educationapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BadEndActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bad_end_cat_dead)

        val catSpirit: ImageView = findViewById(R.id.cat_spirit)
        val youDiedText: TextView = findViewById(R.id.you_died_text)

        // Получаем начальные координаты духа
        catSpirit.post {
            val startX = catSpirit.x
            val startY = catSpirit.y

            // Создаём путь зигзагообразного движения вверх
            val path = Path().apply {
                moveTo(startX, startY)
                val amplitude = 100f
                val stepY = -50f
                for (i in 1..10) {
                    val dx = if (i % 2 == 0) -amplitude else amplitude
                    rLineTo(dx, stepY)
                }
            }

            // Создаём анимацию
            val zigzag = ObjectAnimator.ofFloat(catSpirit, View.X, View.Y, path).apply {
                duration = 3000
                interpolator = LinearInterpolator()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        // Показываем надпись "YOU DIED"
                        youDiedText.visibility = View.VISIBLE
                        youDiedText.alpha = 0f
                        youDiedText.animate()
                            .alpha(1f)
                            .setDuration(1000)
                            .start()
                    }
                })
            }

            // Запускаем анимацию
            zigzag.start()
        }
    }
}
