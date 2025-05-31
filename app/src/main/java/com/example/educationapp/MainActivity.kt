package com.example.educationapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    // Инициализация Firebase Auth
    private lateinit var auth: FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация Firebase
        auth = FirebaseAuth.getInstance()
        
        // Получаем ссылки на элементы разметки
        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login_button)
        val register: TextView = findViewById(R.id.register_button)
        val reset_button: Button = findViewById(R.id.password_reset_button)

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

        reset_button.setOnClickListener {
            val emailText = email.text.toString().trim()

            if (emailText.isEmpty()) {
                Toast.makeText(this, "Введите email для сброса пароля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Условие: есть ли пользователь в бд. Если есть, то отправим письмо, иначе уведомляем его.
            //Должны обновлять данные в бд
            auth.sendPasswordResetEmail(emailText)
                .addOnCompleteListener { resetTask ->
                    if (resetTask.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Письмо для сброса пароля отправлено на $emailText",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Ошибка отправки письма: ${resetTask.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
