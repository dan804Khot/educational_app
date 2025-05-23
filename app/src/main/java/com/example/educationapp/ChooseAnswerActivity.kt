package com.example.educationapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class ChooseAnswerActivity : AppCompatActivity() {

    private lateinit var answerListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_with_answer_choice)

        answerListView = findViewById(R.id.answer_list)

        val answers = listOf("Ответ 1", "Ответ 2", "Ответ 3", "Ответ 4")

        val adapter = AnswerAdapter(this, answers) { selectedAnswer ->
            Toast.makeText(this, "Вы выбрали: $selectedAnswer", Toast.LENGTH_SHORT).show()
        }

        answerListView.adapter = adapter
    }
}

