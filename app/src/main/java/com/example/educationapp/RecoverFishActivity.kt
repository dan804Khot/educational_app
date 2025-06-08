package com.example.educationapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RecoverFishActivity : AppCompatActivity() {

    private lateinit var recoverButton: Button
    private lateinit var closeButton: ImageButton

    // For question screen
    private lateinit var questionText: TextView
    private lateinit var answerInput: EditText
    private lateinit var submitButton: Button
    private val prefs by lazy { getSharedPreferences("game_prefs", MODE_PRIVATE) }

    // Список вопросов для восстановления
    private val recoveryQuestions = listOf(
        RecoveryQuestion(
            id = 1,
            question = "Преобразуйте строку в 'Title Case' (каждое слово с заглавной буквы), " +
                    "но предлоги из списка exceptions должны оставаться в нижнем регистре.\n" +
                    "Пример:\n" +
                    "Вход: 'the quick brown fox jumps over the lazy dog'\n" +
                    "Исключения: ['the', 'over']\n" +
                    "Ответ: 'The Quick Brown Fox Jumps over The Lazy Dog'",
            answer = "public static string TitleCaseWithExceptions(string input, string[] exceptions) {\n" +
                    "    string[] words = input.ToLower().Split(' ');\n" +
                    "    for (int i = 0; i < words.Length; i++) {\n" +
                    "        if (i == 0 || !exceptions.Contains(words[i]))\n" +
                    "            words[i] = char.ToUpper(words[i][0]) + words[i].Substring(1);\n" +
                    "    }\n" +
                    "    return string.Join(\" \", words);\n" +
                    "}"
        ),
        RecoveryQuestion(
            id = 2,
            question = "Зашифруйте строку, где каждая следующая буква сдвигается на значение shift, увеличенное на 1.\n" +
                    "Пример для shift = 1:\n" +
                    "Первая буква: сдвиг 1,\n" +
                    "Вторая: сдвиг 2,\n" +
                    "Третья: сдвиг 3.",
            answer = "public static string DynamicCaesarCipher(string input, int shift) {\n" +
                    "    char[] chars = input.ToCharArray();\n" +
                    "    for (int i = 0; i < chars.Length; i++) {\n" +
                    "        if (!char.IsLetter(chars[i])) continue;\n" +
                    "        char offset = char.IsLower(chars[i]) ? 'a' : 'A';\n" +
                    "        int currentShift = (shift + i) % 26;\n" +
                    "        chars[i] = (char)((chars[i] - offset + currentShift + 26) % 26 + offset);\n" +
                    "    }\n" +
                    "    return new string(chars);\n" +
                    "}"
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_fish)

        recoverButton = findViewById(R.id.recoverButton)
        closeButton = findViewById(R.id.closeButton)

        recoverButton.setOnClickListener {
            setContentView(R.layout.activity_recovery_questions)
            setupQuestionScreen()
        }

        closeButton.setOnClickListener {
            finish()
        }
    }

    private fun setupQuestionScreen() {
        questionText = findViewById(R.id.question_text)
        answerInput = findViewById(R.id.answer_input)
        submitButton = findViewById(R.id.submit_button)
        val closeButton = findViewById<ImageButton>(R.id.closeButton)

        // Получаем текущий вопрос для восстановления
        val currentRecoveryQuestion = getCurrentRecoveryQuestion()
        questionText.text = currentRecoveryQuestion.question

        submitButton.setOnClickListener {
            val userAnswer = answerInput.text.toString().trim()
            if (userAnswer.equals(currentRecoveryQuestion.answer, ignoreCase = true)) {
                // Правильный ответ - восстанавливаем рыбку
                prefs.edit {
                    putBoolean("fish_recovered", true)
                    // Увеличиваем индекс вопроса для следующего раза
                    putInt("recovery_question_index",
                        (prefs.getInt("recovery_question_index", 0) + 1) % recoveryQuestions.size)
                }
                Toast.makeText(this, "Рыбка восстановлена!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Неправильный ответ, попробуйте еще раз", Toast.LENGTH_SHORT).show()
            }
        }

        closeButton.setOnClickListener {
            finish()
        }
    }

    private fun getCurrentRecoveryQuestion(): RecoveryQuestion {
        val questionIndex = prefs.getInt("recovery_question_index", 0)
        return recoveryQuestions[questionIndex % recoveryQuestions.size]
    }
}

data class RecoveryQuestion(
    val id: Int,
    val question: String,
    val answer: String
)