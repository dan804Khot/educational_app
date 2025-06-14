package com.example.educationapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

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
                        startActivity(Intent(this, ChoiceOfDisciplinesActivity::class.java))
                    } else {
                        // Проверяем, совпадает ли email и password с сохраненным в SharedPreferences
                        val savedEmail = sharedPref.getString("user_email", "")
                        val saved_password = sharedPref.getString("password", "")
                        if (savedEmail != null && savedEmail.isNotEmpty() && savedEmail != emailText) {
                            Toast.makeText(
                                this,
                                "Такого пользователя не существует",
                                Toast.LENGTH_LONG
                            ).show()
                            return@addOnCompleteListener
                        }
                        else if (saved_password != passwordText){
                            Toast.makeText(
                                this,
                                "Неверный пароль",
                                Toast.LENGTH_LONG
                            ).show()
                            return@addOnCompleteListener
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
                showError("Введите email")
                return@setOnClickListener
            }

            // Проверяем, совпадает ли email с сохраненным в SharedPreferences
            val savedEmail = sharedPref.getString("user_email", "")
            if (savedEmail != null && savedEmail.isNotEmpty() && savedEmail != emailText) {
                Toast.makeText(
                    this,
                    "Такого пользователя не существует",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(emailText)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Письмо отправлено", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        showError("Ошибка: ${e.message}")
                    }
            }

        }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
