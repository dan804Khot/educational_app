package com.example.educationapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.content.edit

class LevelMapActivity : AppCompatActivity() {
    //private val unlockedLevels = mutableListOf(true, false, false, false, false)
    //private val completedLevels = mutableListOf(false, false, false, false, false)
    private val prefs by lazy { getSharedPreferences("LevelProgress", Context.MODE_PRIVATE) }
    private val prefs2 by lazy { getSharedPreferences("game_prefs", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_map_activity)

        // Загружаем текущий прогресс
        val currentLevel = prefs.getInt("current_level", 1)
        updateProgress(currentLevel)

        // Проверяем, нужно ли показывать экран завершения
        if (allLevelsCompleted() && !prefs.getBoolean("course_completed_shown", false)) {
            showWin()
        }

        val worldName: EditText = findViewById(R.id.world_name)
        worldName.setText("Программирование C#")
        val backBtn: ImageButton = findViewById(R.id.back_button)

        val level1Completed = prefs.getBoolean("level1_completed", false)
        val level2Unlocked = prefs.getBoolean("level2_unlocked", false)
        val level2Completed = prefs.getBoolean("level2_completed", false)
        val level3Unlocked = prefs.getBoolean("level3_unlocked", false)
        var level3Completed = prefs.getBoolean("level3_completed", false)
        var level4Unlocked = prefs.getBoolean("level4_unlocked", false)
        var level4Completed = prefs.getBoolean("level4_completed", false)
        var level5Unlocked = prefs.getBoolean("level5_unlocked", false)
        var level5Completed = prefs.getBoolean("level5_completed", false)

        // Кнопка Назад
        backBtn.setOnClickListener {
            finish()
        }

        // Кнопки уровней
        // Level 1 button
        findViewById<FrameLayout>(R.id.level1).setOnClickListener {
            if (level1Completed) {
                setContentView(R.layout.activity_level_completed)
                findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
                    finish()
                    startActivity(Intent(this, LevelMapActivity::class.java))
                }
                findViewById<Button>(R.id.completedButton).setOnClickListener {
                    startActivity(Intent(this, LevelMapActivity::class.java))
                }
            } else {
                setContentView(R.layout.activity_level_not_completed)
                findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
                    finish()
                    startActivity(Intent(this, LevelMapActivity::class.java))
                }
                findViewById<Button>(R.id.completedButton).setOnClickListener {
                    startActivity(Intent(this, ChooseAnswerActivity::class.java))
                }
            }
        }

        // Level 2 button
        findViewById<FrameLayout>(R.id.level2).setOnClickListener {
            if (level2Unlocked) {
                if (level2Completed) {
                    setContentView(R.layout.activity_level_completed)
                    findViewById<TextView>(R.id.levelTitle).text = "Уровень 2"
                    findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
                        finish()
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                    findViewById<Button>(R.id.completedButton).setOnClickListener {
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                } else {
                    setContentView(R.layout.activity_level_not_completed)
                    findViewById<TextView>(R.id.levelTitle).text = "Уровень 2"
                    findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
                        finish()
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                    findViewById<Button>(R.id.completedButton).setOnClickListener {
                        startActivity(Intent(this, ChooseAnswerActivity2::class.java))
                    }
                }
            } else {
                Toast.makeText(this, "Уровень заблокирован", Toast.LENGTH_SHORT).show()
            }
        }

        // Level 3 button
        findViewById<FrameLayout>(R.id.level3).setOnClickListener {
            if (level3Unlocked) {
                if (level3Completed) {
                    setContentView(R.layout.activity_level_completed)
                    findViewById<TextView>(R.id.levelTitle).text = "Уровень 3"
                    findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
                        finish()
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                    findViewById<Button>(R.id.completedButton).setOnClickListener {
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                } else {
                    setContentView(R.layout.activity_level_not_completed)
                    updateProgress(3)
                    findViewById<TextView>(R.id.levelTitle).text = "Уровень 3"
                    findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
                        finish()
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                    findViewById<Button>(R.id.completedButton).setOnClickListener {
                        // Сохраняем изменения в SharedPreferences
                        prefs.edit {
                            putBoolean("level3_completed", true)
                            putBoolean("level4_unlocked", true)
                            putInt("current_level", 3)
                            putInt("levels_completed", prefs.getInt("levels_completed", 0) + 1)
                        }
                        finish()
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                }
            } else {
                Toast.makeText(this, "Уровень заблокирован", Toast.LENGTH_SHORT).show()
            }
        }

        // Level 4 button
        findViewById<FrameLayout>(R.id.level4).setOnClickListener {
            if (level4Unlocked) {
                if (level4Completed) {
                    setContentView(R.layout.activity_level_completed)
                    findViewById<TextView>(R.id.levelTitle).text = "Уровень 4"
                    findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
                        finish()
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                    findViewById<Button>(R.id.completedButton).setOnClickListener {
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                } else {
                    setContentView(R.layout.activity_level_not_completed)
                    updateProgress(3)
                    findViewById<TextView>(R.id.levelTitle).text = "Уровень 4"
                    findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
                        finish()
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                    findViewById<Button>(R.id.completedButton).setOnClickListener {
                        // Сохраняем изменения в SharedPreferences
                        prefs.edit {
                            putBoolean("level4_completed", true)
                            putBoolean("level5_unlocked", true)
                            putInt("current_level", 4)
                            putInt("levels_completed", prefs.getInt("levels_completed", 0) + 1)
                        }
                        finish()
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                }
            } else {
                Toast.makeText(this, "Уровень заблокирован", Toast.LENGTH_SHORT).show()
            }
        }

        // Level 5 button
        findViewById<FrameLayout>(R.id.level5).setOnClickListener {
            if (level5Unlocked) {
                if (level5Completed) {
                    setContentView(R.layout.activity_level_completed)
                    findViewById<TextView>(R.id.levelTitle).text = "Уровень 5"
                    findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
                        finish()
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                    findViewById<Button>(R.id.completedButton).setOnClickListener {
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                } else {
                    setContentView(R.layout.activity_level_not_completed)
                    updateProgress(3)
                    findViewById<TextView>(R.id.levelTitle).text = "Уровень 5"
                    findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
                        finish()
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                    findViewById<Button>(R.id.completedButton).setOnClickListener {
                        // Сохраняем изменения в SharedPreferences
                        prefs.edit {
                            putBoolean("level5_completed", true)
                            putInt("current_level", 5)
                            putInt("levels_completed", prefs.getInt("levels_completed", 0) + 1)
                        }
                        finish()
                        startActivity(Intent(this, LevelMapActivity::class.java))
                    }
                }
            } else {
                Toast.makeText(this, "Уровень заблокирован", Toast.LENGTH_SHORT).show()
            }
        }

        updateLevelButtonsState(
            level1Completed,
            level2Unlocked, level2Completed,
            level3Unlocked, level3Completed,
            level4Unlocked, level4Completed,
            level5Unlocked, level5Completed
        )

        // Рейтинг
        findViewById<Button>(R.id.rating_button).setOnClickListener {
            startActivity(Intent(this, RatingActivity::class.java))
        }

        // Прогресс
        findViewById<Button>(R.id.progress_button).setOnClickListener {
            startActivity(Intent(this, UserStatisticsActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        if (!prefs2.getBoolean("fish_recovered", false)) {
            startActivity(Intent(this, RecoverFishActivity::class.java))
            finish()
        }
    }

    private fun updateProgress(currentLevel: Int) {
        val progressText = "$currentLevel/5"
        findViewById<TextView>(R.id.progress_text).text = progressText
        prefs.edit { putInt("current_level", currentLevel) }
    }


    private fun updateLevelButtonsState(
        level1Completed: Boolean,
        level2Unlocked: Boolean, level2Completed: Boolean,
        level3Unlocked: Boolean, level3Completed: Boolean,
        level4Unlocked: Boolean, level4Completed: Boolean,
        level5Unlocked: Boolean, level5Completed: Boolean
    ) {
        val levelBackgrounds = listOf(
            findViewById<ImageView>(R.id.level1_background),
            findViewById<ImageView>(R.id.level2_background),
            findViewById<ImageView>(R.id.level3_background),
            findViewById<ImageView>(R.id.level4_background),
            findViewById<ImageView>(R.id.level5_background)
        )

        // Update level 1
        levelBackgrounds[0].setImageResource(
            if (level1Completed) R.drawable.green_level_check else R.drawable.green_level_check
        )

        // Update level 2
        levelBackgrounds[1].setImageResource(
            if (level2Unlocked) R.drawable.green_level_check else R.drawable.red_check
        )

        // Update level 3
        levelBackgrounds[2].setImageResource(
            if (level3Unlocked) R.drawable.green_level_check else R.drawable.red_check
        )

        // Update level 4
        levelBackgrounds[3].setImageResource(
            if (level4Unlocked) R.drawable.green_level_check else R.drawable.red_check
        )

        // Update level 5
        levelBackgrounds[4].setImageResource(
            if (level5Unlocked) R.drawable.green_level_check else R.drawable.red_check
        )

        if (level1Completed && level2Completed && level3Completed &&
            level4Completed && level5Completed
        ) {
            showWin()
        }
    }

    private fun allLevelsCompleted(): Boolean {
        return prefs.getBoolean("level1_completed", false) &&
                prefs.getBoolean("level2_completed", false) &&
                prefs.getBoolean("level3_completed", false) &&
                prefs.getBoolean("level4_completed", false) &&
                prefs.getBoolean("level5_completed", false)
    }

    private fun showWin() {
        // Помечаем, что экран завершения был показан
        prefs.edit { putBoolean("course_completed_shown", true) }

        val intent = Intent(this, CourseCompletedActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_COURSE_COMPLETED && resultCode == RESULT_OK) {
            val shouldNotReopen = data?.getBooleanExtra("shouldNotReopen", false) ?: false
            if (shouldNotReopen) {
                startActivity(Intent(this, LevelMapActivity::class.java))
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_COURSE_COMPLETED = 1001
    }
}
