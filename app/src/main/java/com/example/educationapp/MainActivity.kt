package com.example.educationapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    // Инициализация Firebase Auth
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

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
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener { loginTask ->
                    if (loginTask.isSuccessful) {
                        // Успешный вход
                        startActivity(Intent(this, ChoiceOfDisciplinesActivity::class.java))
                    } else {
                        // Анализируем конкретную ошибку
                        when (loginTask.exception) {
                            is FirebaseAuthInvalidUserException -> {
                                Toast.makeText(
                                    this,
                                    "Пользователь с таким email не найден",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                Toast.makeText(
                                    this,
                                    "Неверный пароль",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                                Toast.makeText(
                                    this,
                                    "Ошибка входа: ${loginTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
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

            // Сначала проверяем в Firebase Auth
            auth.fetchSignInMethodsForEmail(emailText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val signInMethods = task.result?.signInMethods
                        if (!signInMethods.isNullOrEmpty()) {
                            // Пользователь есть в Auth - отправляем письмо
                            sendResetEmail(emailText)
                        } else {
                            // Пользователя нет в Auth, но проверяем в Firestore
                            checkInFirestore(emailText)
                        }
                    } else {
                        showError("Ошибка проверки email: ${task.exception?.message}")
                    }
                }
        }
    }
    private fun sendResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Письмо отправлено. Проверьте почту $email",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    showError("Не удалось отправить письмо: ${task.exception?.message}")
                }
            }
    }

    private fun checkInFirestore(email: String) {
        db.collection("users").document(email).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    showError("Аккаунт существует, но требуется верификация email")
                } else {
                    showError("Пользователь с email $email не найден")
                }
            }
            .addOnFailureListener { exception ->
                showError("Ошибка проверки пользователя: ${exception.message}")
                exception.printStackTrace()
                Log.d("CHECK", "${exception.message}")
            }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
