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


class ChooseAnswerActivity2 : AppCompatActivity() {

    private lateinit var answerListView: ListView
    private lateinit var questions: List<Question>
    private var currentQuestionIndex = 0
    private var correctAnswersCount = 0
    private var secondAttemptsFailed = 0
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
                "Дана часть кода. Что он должен вывести?\nКод:\nstring s = \"Hello\".Substring(1, 3);\nConsole.WriteLine(s);",
                QuestionType.WITH_ANSWERS,
                listOf(
                    "ell",
                    "Hel",
                    "llo"
                ),
                "ell",
                "Метод Substring(1,3) возвращает подстроку длиной 3 символа, начиная с индекса 1."
            ),
            Question(
                2,
                "Какой метод удаляет пробелы в начале и конце строки?",
                QuestionType.WITH_ANSWERS,
                listOf(
                    "Trim",
                    "RemoveSpaces",
                    "Strip"
                ),
                "Trim",
                "Метод Trim() удаляет все начальные и конечные пробельные символы из строки."
            ),
            Question(
                3,
                "Какой код удаляет все пробелы из строки str?\nstring str = \"Hello, World!\";\nstring noSpaces = ?",
                QuestionType.WITH_ANSWERS,
                listOf(
                    "str.TrimAll()",
                    "str.Replace(\" \", \"\")",
                    "str.RemoveSpaces()"
                ),
                "str.Replace(\" \", \"\")",
                "Метод Replace() заменяет все вхождения указанной подстроки на другую подстроку."
            ),
            Question(
                4,
                "Напишите код, который выводит длину строки str.\nПеременные: string str = \"Hello\";",
                QuestionType.WITHOUT_ANSWERS,
                null,
                "Console.WriteLine(str.Length);",
                "Свойство Length возвращает количество символов в строке."
            ),
            Question(
                5,
                "Проверьте, содержит ли строка str подстроку \"abc\".\nПеременные: string str = \"abcdef\";",
                QuestionType.WITHOUT_ANSWERS,
                null,
                "Console.WriteLine(str.Contains(\"abc\"));",
                "Метод Contains() возвращает true, если строка содержит указанную подстроку."
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
            1 -> R.drawable.head_fish
            2 -> R.drawable.half_fish
            3 -> R.drawable.fish
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
        taskNumber.text = "${question.attempts}/2"

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
                    question.attempts++
                    taskNumber.text = "${question.attempts}/2"
                    if (question.attempts >= 2) {
                        secondAttemptsFailed++
                        if (secondAttemptsFailed >= 3) {
                            showAngryTeacher()
                            return@setOnClickListener
                        }
                    }
                    if (question.attempts < 2) {
                        // Stay on same question for second attempt
                        return@setOnClickListener
                    }
                    nextQuestion()
                }
            } else {
                Toast.makeText(this, "Выберите ответ", Toast.LENGTH_SHORT).show()
            }
        }

        nextButton.setOnClickListener {
            question.attempts+=2
            taskNumber.text = "${question.attempts}/2"
            if (question.attempts >= 2) {
                secondAttemptsFailed++
                if (secondAttemptsFailed >= 3) {
                    showAngryTeacher()
                    return@setOnClickListener
                }
            }
            nextQuestion()
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
        answerBox.setText("")
        taskNumber.text = "${question.attempts}/2"

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
                question.attempts++
                taskNumber.text = "${question.attempts}/2"
                if (question.attempts >= 2) {
                    secondAttemptsFailed++
                    if (secondAttemptsFailed >= 3) {
                        showAngryTeacher()
                        return@setOnClickListener
                    }
                }
                if (question.attempts < 2) {
                    return@setOnClickListener
                }
                nextQuestion()
            }
        }

        nextButton.setOnClickListener {
            question.attempts+=2
            taskNumber.text = "${question.attempts}/2"
            if (question.attempts >= 2) {
                secondAttemptsFailed++
                if (secondAttemptsFailed >= 3) {
                    showAngryTeacher()
                    return@setOnClickListener
                }
            }
            nextQuestion()
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
        if (correctAnswersCount >= 3 && secondAttemptsFailed < 3) {

            val sharedPref = getSharedPreferences("LevelProgress", Context.MODE_PRIVATE)
            val currentScore = sharedPref.getInt("user_score", 0)
            val pointsEarned = when (correctAnswersCount) {
                3 -> 100
                4 -> 150
                5 -> 200
                else -> 50
            }

            sharedPref.edit {
                putInt("user_score", currentScore + pointsEarned)
                apply()
            }

            prefs.edit {
                putBoolean("level2_completed", true)
                putBoolean("level3_unlocked", true)
                putInt("current_level", 3)
                putInt("levels_completed", prefs.getInt("levels_completed", 0) + 1)
                apply()
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
        val teacherImage = ImageView(this).apply {
            setImageResource(R.drawable.cat_teacher)
            layoutParams = FrameLayout.LayoutParams(
                (resources.displayMetrics.widthPixels * 0.8).toInt(),
                (resources.displayMetrics.heightPixels * 0.4).toInt()
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                bottomMargin = (resources.displayMetrics.heightPixels * 0.05).toInt()
            }
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        val speechBubble = TextView(this).apply {
            text = "Я тобой недоволен!\nТы исчерпал все попытки!"
            setTextColor(Color.BLACK)
            background = ContextCompat.getDrawable(context, R.drawable.speech_bubble)
            gravity = Gravity.CENTER
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setPadding(48, 48, 48, 48)
            layoutParams = FrameLayout.LayoutParams(
                (resources.displayMetrics.widthPixels * 0.8).toInt(),
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                bottomMargin = (resources.displayMetrics.heightPixels * 0.45).toInt()
            }
        }

        val overlayLayout = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.argb(180, 0, 0, 0))
            addView(speechBubble)
            addView(teacherImage)
        }

        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(overlayLayout)

        Handler(Looper.getMainLooper()).postDelayed({
            rootView.removeView(overlayLayout)
            showBadEnd()
        }, 2000)
    }

    private fun showBadEnd() {
        prefs.edit {
            putBoolean("fish_recovered", false)
            putInt("failed_attempts", 0)
        }
        val intent = Intent(this, BadEndActivity::class.java)
        startActivity(intent)
        finish()
    }
}


