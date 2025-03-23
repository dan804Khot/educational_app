package com.example.educationapp

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

        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login_button)
        val register: TextView = findViewById(R.id.register)

        loginButton.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Вход выполнен!", Toast.LENGTH_SHORT).show()
            }
        }

        register.setOnClickListener {
            Toast.makeText(this, "Регистрация пока не реализована", Toast.LENGTH_SHORT).show()
        }
    }
}