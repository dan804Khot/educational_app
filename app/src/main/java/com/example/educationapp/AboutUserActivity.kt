package com.example.educationapp

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException

class AboutMeActivity : Activity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var groupEditText: EditText
    private lateinit var aboutTextEditText: EditText
    private lateinit var backButton: ImageButton
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_user)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        fullNameEditText = findViewById(R.id.full_name)
        groupEditText = findViewById(R.id.group)
        aboutTextEditText = findViewById(R.id.about_text)
        backButton = findViewById(R.id.back_button)
        saveButton = findViewById(R.id.about_button)

        // Загружаем данные пользователя
        loadUserData()

        backButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            saveAboutText()
        }
    }

    private fun loadUserData() {
        val email = auth.currentUser?.email ?: run {
            Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        db.collection("users").document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val fullName = "${document.getString("lastName") ?: ""} " +
                            "${document.getString("firstName") ?: ""} " +
                            "${document.getString("middleName") ?: ""}"

                    fullNameEditText.setText(fullName.trim())
                    groupEditText.setText(document.getString("group") ?: "")
                    aboutTextEditText.setText(document.getString("about") ?: "")
                } else {
                    Toast.makeText(this, "Данные пользователя не найдены", Toast.LENGTH_SHORT)
                        .show()
                    loadFromSharedPreferences()
                }
            }
            .addOnFailureListener { e ->
                if (e is IOException) {
                    Toast.makeText(this, "Проблемы с интернет-соединением", Toast.LENGTH_SHORT)
                        .show()
                    loadFromSharedPreferences()
                } else {
                    Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                    loadFromSharedPreferences()
                }
            }
    }

        private fun saveAboutText() {
            val email = auth.currentUser?.email ?: run {
                Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show()
                return
            }

            val aboutText = aboutTextEditText.text.toString().trim()

            db.collection("users").document(email)
                .update("about", aboutText)
                .addOnSuccessListener {
                    Toast.makeText(this, "Данные успешно сохранены", Toast.LENGTH_SHORT).show()
                    sharedPref.edit().putString("about_text", aboutText).apply()
                }
                .addOnFailureListener { e ->
                    if (e is IOException) {
                        Toast.makeText(this, "Проблемы с интернет-соединением", Toast.LENGTH_SHORT)
                            .show()
                        loadFromSharedPreferences()
                    } else {
                        Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                        loadFromSharedPreferences()
                    }
                }
        }
    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadFromSharedPreferences() {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Получаем данные из SharedPreferences
        val lastName = sharedPref.getString("last_name", "") ?: ""
        val firstName = sharedPref.getString("first_name", "") ?: ""
        val middleName = sharedPref.getString("middle_name", "") ?: ""
        val group = sharedPref.getString("group", "") ?: ""
        val about = sharedPref.getString("about_text", "") ?: ""
        val email = sharedPref.getString("user_email", "") ?: ""

        val fullName = "$lastName $firstName $middleName".trim()

        // Устанавливаем значения в поля
        fullNameEditText.setText(fullName)
        groupEditText.setText(group)
        aboutTextEditText.setText(about)

    }
}
