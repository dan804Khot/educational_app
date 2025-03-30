package com.example.educationapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        // Найдем элементы интерфейса
        val email: EditText = findViewById(R.id.email)
        val firstName: EditText = findViewById(R.id.first_name)
        val lastName: EditText = findViewById(R.id.last_name)
        val middleName: EditText = findViewById(R.id.middle_name)
        val group: EditText = findViewById(R.id.group)
        val registerButton: Button = findViewById(R.id.register_button)

        // Обработчик кнопки регистрации
        registerButton.setOnClickListener {
            val emailText = email.text.toString().trim()
            val firstNameText = firstName.text.toString().trim()
            val lastNameText = lastName.text.toString().trim()
            val middleNameText = middleName.text.toString().trim()
            val groupText = group.text.toString().trim()

            if (emailText.isEmpty() || firstNameText.isEmpty() || lastNameText.isEmpty() ||
                middleNameText.isEmpty() || groupText.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                finish() // Закрываем активность после успешной регистрации
            }
        }
    }
}
