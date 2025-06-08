package com.example.educationapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.SyncStateContract
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit


class ChooseAnswerActivity : AppCompatActivity() {

    private lateinit var answerListView: ListView
    private lateinit var questions: List<Question>
    private var currentQuestionIndex = 0
    private var correctAnswersCount = 0
    private var wrongAnswersCount = 0
    private val prefs by lazy { getSharedPreferences("LevelProgress", Context.MODE_PRIVATE) }
    private lateinit var fishIcon: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about_teacher)
        initializeQuestions()
        val speech = findViewById<TextView>(R.id.speechBubble)
        speech.text = "Опытный кот-преподаватель с 9 жизнями опыта в обучении программированию C#. Любит объяснять сложные концепции простыми словами и поощрять студентов рыбками за успехи."
        val continueButton = findViewById<Button>(R.id.buttonNext)
        continueButton.setOnClickListener {
            showQuestionScreen()
        }
        val exitButton = findViewById<Button>(R.id.exit_button)
        exitButton.setOnClickListener {
            finish();
        }

    }

    private fun initializeQuestions() {
        questions = listOf(
            Question(
                1,
                "Что делает код?\nint[] scores = {85, 90, 78, 92};\nConsole.WriteLine(scores[0]);",
                QuestionType.WITH_ANSWERS,
                listOf(
                    "Выводит все элементы",
                    "Выводит элемент с индексом 0",
                    "Выводит элемент с индексом 1",
                    "Выводит ошибку"
                ),
                "Выводит элемент с индексом 0",
                "В C# массивы индексируются с 0. scores[0] обращается к первому элементу массива."
            ),
            Question(
                2,
                "Для создания константы в C# используется ключевое слово ________",
                QuestionType.WITH_ANSWERS,
                listOf("var", "let", "const", "static"),
                "const",
                "Ключевое слово const используется для объявления констант в C#."
            ),
            Question(
                3,
                "Найдите и исправьте ошибку.\nconst double PI;\nPI = 3.14;\nConsole.WriteLine(PI);",
                QuestionType.WITHOUT_ANSWERS,
                null,
                "const double PI=3.14;\nConsole.WriteLine(PI);",
                "Константы в C# должны быть инициализированы при объявлении."
            ),
            Question(
                4,
                "При объявлении массива, размер массива указывается внутри __________.",
                QuestionType.WITH_ANSWERS,
                listOf("{ }", "( )", "[ ]", "< >"),
                "[ ]",
                "В C# размер массива указывается в квадратных скобках."
            ),
            Question(
                5,
                "Создайте переменную num со значением 20. Добавьте к ней число 5. Используйте сокращенную запись.",
                QuestionType.WITHOUT_ANSWERS,
                null,
                "int num = 20;\nnum += 5;",
                "Сокращенные операторы присваивания (+=, -=, *=, /=)."
            )
        )
    }

    private fun showQuestionScreen() {
        if (currentQuestionIndex >= questions.size) {
            checkLevelCompletion()
            return
        }

        val question = questions[currentQuestionIndex]

        if (question.type == QuestionType.WITH_ANSWERS) {
            setContentView(R.layout.activity_question_with_answer_choice)
            fishIcon = findViewById(R.id.fish_icon)
            updateFishProgress()
            setupQuestionWithAnswers(question)
        } else {
            setContentView(R.layout.activity_question_with_no_answer_choice)
            fishIcon = findViewById(R.id.fish_icon)
            updateFishProgress()
            setupQuestionWithoutAnswers(question)
        }
    }

    private fun updateFishProgress() {
        if (correctAnswersCount >= 3) {
            fishIcon.setImageResource(R.drawable.fish)
            return
        }

        val progress = when (correctAnswersCount) {
            0 -> R.drawable.fish_bones
            1 -> R.drawable.fish
            2 -> R.drawable.fish
            else -> R.drawable.fish_bones
        }

        fishIcon.setImageResource(progress)
    }

    private fun setupQuestionWithAnswers(question: Question) {
        val questionLabel = findViewById<TextView>(R.id.question_label)
        val theoryBlock = findViewById<TextView>(R.id.theory_block)
        val taskNumber = findViewById<TextView>(R.id.task_number)

        questionLabel.text = question.text
        theoryBlock.text = question.theory
        taskNumber.text = "${wrongAnswersCount}/2"

        answerListView = findViewById(R.id.answer_list)
        val checkButton = findViewById<Button>(R.id.check_question)
        val nextButton = findViewById<Button>(R.id.next_question)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, question.answers!!)
        answerListView.adapter = adapter

        var selectedAnswer: String? = null

        answerListView.setOnItemClickListener { _, _, position, _ ->
            selectedAnswer = question.answers[position]
        }

        checkButton.setOnClickListener {
            if (selectedAnswer != null) {
                if (selectedAnswer == question.correctAnswer) {
                    correctAnswersCount++
                    updateFishProgress()
                    nextQuestion()
                } else {
                    wrongAnswersCount++
                    if (wrongAnswersCount == 2) {
                        showAngryTeacher()
                    }
                    else{
                        nextQuestion()
                    }
                }
            } else {
                Toast.makeText(this, "Выберите ответ", Toast.LENGTH_SHORT).show()
            }
        }

        nextButton.setOnClickListener {
            wrongAnswersCount = 2 // Сразу исчерпываем попытки
            showAngryTeacher()
        }

        findViewById<Button>(R.id.main_menu_button).setOnClickListener {
            finish()
        }
    }

    private fun setupQuestionWithoutAnswers(question: Question) {
        val questionLabel = findViewById<TextView>(R.id.question_label)
        val theoryBlock = findViewById<TextView>(R.id.theory_block)
        val answerBox = findViewById<EditText>(R.id.answer_box)
        val taskNumber = findViewById<TextView>(R.id.task_number)

        questionLabel.text = question.text
        theoryBlock.text = question.theory
        taskNumber.text = "${wrongAnswersCount}/2"
        answerBox.setText("")

        val checkButton = findViewById<Button>(R.id.check_question)
        val nextButton = findViewById<Button>(R.id.next_question)

        checkButton.setOnClickListener {
            val userAnswer = answerBox.text.toString().trim()
            if (userAnswer == question.correctAnswer) {
                correctAnswersCount++
                updateFishProgress()
                nextQuestion()
                Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
            } else {
                wrongAnswersCount++
                if (wrongAnswersCount == 2) {
                    showAngryTeacher()
                }
                else{
                    nextQuestion()
                }
            }
        }


        nextButton.setOnClickListener {
            wrongAnswersCount = 2 // Сразу исчерпываем попытки
            showAngryTeacher()
        }

        findViewById<Button>(R.id.main_menu_button).setOnClickListener {
            finish()
        }
    }


    private fun nextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            showQuestionScreen()
        } else {
            checkLevelCompletion()
        }
    }

    private fun checkLevelCompletion() {
        if (correctAnswersCount >= 3) {
            val currentScore = prefs.getInt("user_score", 0)
            val pointsEarned = when (correctAnswersCount) {
                3 -> 100
                4 -> 150
                5 -> 200
                else -> 50
            }

            prefs.edit {
                putBoolean("level1_completed", true)
                putBoolean("level2_unlocked", true)
                putInt("user_score", currentScore + pointsEarned)
                putInt("levels_completed", prefs.getInt("levels_completed", 0) + 1)
            }
            showGoodEnd()
        } else {
            showBadEnd()
        }
    }

    private fun showGoodEnd() {
        val intent = Intent(this, GoodEndActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showAngryTeacher() {
        // Создаем ImageView для кота-учителя
        val teacherImage = ImageView(this).apply {
            setImageResource(R.drawable.cat_teacher)
            layoutParams = FrameLayout.LayoutParams(
                (resources.displayMetrics.widthPixels * 0.8).toInt(), // 80% от ширины экрана
                (resources.displayMetrics.heightPixels * 0.4).toInt() // 40% от высоты экрана
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                bottomMargin = (resources.displayMetrics.heightPixels * 0.05).toInt() // 5% от высоты экрана
            }
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        // Создаем TextView для облачка с текстом
        val speechBubble = TextView(this).apply {
            text = "Я тобой недоволен!\nТы исчерпал все попытки!"
            setTextColor(Color.BLACK)
            background = ContextCompat.getDrawable(context, R.drawable.speech_bubble)
            gravity = Gravity.CENTER
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setPadding(48, 48, 48, 48) // Увеличиваем отступы
            layoutParams = FrameLayout.LayoutParams(
                (resources.displayMetrics.widthPixels * 0.8).toInt(), // 80% от ширины экрана
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                bottomMargin = (resources.displayMetrics.heightPixels * 0.45).toInt() // 45% от высоты экрана
            }
        }

        // Создаем контейнер для оверлея
        val overlayLayout = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.argb(180, 0, 0, 0)) // Более темный полупрозрачный фон
            addView(speechBubble)
            addView(teacherImage)
        }

        // Добавляем оверлей к layout
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(overlayLayout)

        // Через 2 секунды показываем плохую концовку
        Handler(Looper.getMainLooper()).postDelayed({
            rootView.removeView(overlayLayout)
            showBadEnd()
        }, 2000)
    }

    private fun showBadEnd() {
        prefs.edit {
            putBoolean("fish_recovered", false)
        }
        val intent = Intent(this, BadEndActivity::class.java)
        startActivity(intent)
        finish()
    }

}
data class Question(
    val id: Int,
    val text: String,
    val type: QuestionType,
    val answers: List<String>? = null,
    val correctAnswer: String,
    val theory: String = ""
)

enum class QuestionType {
    WITH_ANSWERS, WITHOUT_ANSWERS
}
