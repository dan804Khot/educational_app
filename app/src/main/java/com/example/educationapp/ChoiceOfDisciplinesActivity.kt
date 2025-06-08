package com.example.educationapp

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ChoiceOfDisciplinesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_of_disciplines)

        val startButton: Button = findViewById(R.id.start_button)
        val spinner: Spinner = findViewById(R.id.discipline_spinner)
        val titleText: TextView = findViewById(R.id.title_text)

        // Устанавливаем градиентный текст и тень
        titleText.post {
            val paint = titleText.paint
            val width = paint.measureText(titleText.text.toString())

            val shader = LinearGradient(
                0f, 0f, width, titleText.textSize,
                intArrayOf(Color.WHITE, Color.LTGRAY),
                null,
                Shader.TileMode.CLAMP
            )
            titleText.paint.shader = shader
            titleText.setShadowLayer(10f, 0f, 0f, Color.WHITE)
        }

        // список дисциплин
        val disciplines = arrayOf("Программирование C#", "Программирование Python", "Программирование Kotlin")

        // адаптер для Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, disciplines)
        spinner.adapter = adapter

        // обработчик выбора дисциплины
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDiscipline = parent.getItemAtPosition(position).toString()
                titleText.text = selectedDiscipline
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // кнопка "Начать"
        startButton.setOnClickListener {
            if (titleText.text != "Программирование C#")
            {
                Toast.makeText(this, "Упс...Данной дисциплины пока нет", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this, LevelMapActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
