package com.example.educationapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Получаем ссылки на элементы разметки
        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login_button)
        val register: TextView = findViewById(R.id.register)

        // Обработчик кнопки входа
        loginButton.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                //если данные введены, переходим на ChoiceOfDisciplinesActivity
                val intent = Intent(this, ChoiceOfDisciplinesActivity::class.java)
                startActivity(intent)
            }
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
