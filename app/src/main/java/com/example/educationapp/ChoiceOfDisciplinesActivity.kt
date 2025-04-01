package com.example.educationapp

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

        //список дисциплин
        val disciplines = arrayOf("1", "2", "3", "4", "5")

        //адаптер для Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, disciplines)
        spinner.adapter = adapter

        //обработчик выбора дисциплины
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDiscipline = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@ChoiceOfDisciplinesActivity, "Вы выбрали: $selectedDiscipline", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //кнопка "Начать"
        startButton.setOnClickListener {
            Toast.makeText(this, "Вы начали изучение!", Toast.LENGTH_SHORT).show()
        }
    }
}
