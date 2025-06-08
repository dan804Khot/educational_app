package com.example.educationapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.util.Patterns
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AppCompatActivity


class RegisterActivity : AppCompatActivity() {
    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var email: EditText
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var middleName: EditText
    private lateinit var group: EditText
    private lateinit var password: EditText
    private lateinit var registerButton: Button
    private lateinit var backButton: ImageButton
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        // Инициализация Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Найдем элементы интерфейса
        email = findViewById(R.id.email)
        firstName = findViewById(R.id.first_name)
        lastName = findViewById(R.id.last_name)
        middleName = findViewById(R.id.middle_name)
        group = findViewById(R.id.group)
        password = findViewById(R.id.password)
        registerButton = findViewById(R.id.register_button)
        backButton = findViewById(R.id.back_button)

        //Ограничения на длину
        firstName.filters = arrayOf(InputFilter.LengthFilter(20))
        lastName.filters = arrayOf(InputFilter.LengthFilter(20))
        middleName.filters = arrayOf(InputFilter.LengthFilter(30))
        
        // Обработчик кнопки регистрации
        registerButton.setOnClickListener {
            CheckAllConditions(this)
        }

        // Кнопка "Назад"
        backButton.setOnClickListener {
            finish()
        }
    }

    //Проверка всех условий
    private fun CheckAllConditions(context: Context) {
        var allConditions = true

        if (!isNullFields(
                this,
                email,
                firstName,
                lastName,
                middleName,
                group,
                password
            )
        ) {
            allConditions = false
        }


        if (!IsValidEmail(this, email)) {
            allConditions = false
        }

        if (!IsValidGroup(group.text.toString())) {
            group.error = "Неправильный формат. Ожидалось 'ПМИ-22-1' или 'ПМИ-22'"
            allConditions = false
        }
        if (password.text.length < 6) {
            password.error = "Пароль должен содержать минимум 6 символов"
            allConditions = false
        }
        if (allConditions) {
            val emailText = email.text.toString().trim()
            val firstNameText = firstName.text.toString().trim()
            val lastNameText = lastName.text.toString().trim()
            val middleNameText = middleName.text.toString().trim()
            val groupText = group.text.toString().trim()
            val passwordText = password.text.toString().trim()
            checkUserExists(emailText) { exists ->
                if (exists) {
                    Toast.makeText(
                        context,
                        "Пользователь с таким email уже существует",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Регистрация в Firebase
                    Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                    registerUser(
                        emailText,
                        passwordText
                    )
                    // Сохраняем все данные в SharedPreferences
                    saveUserDataToSharedPref(
                        email = email.text.toString().trim(),
                        firstName = firstName.text.toString().trim(),
                        lastName = lastName.text.toString().trim(),
                        middleName = middleName.text.toString().trim(),
                        group = group.text.toString().trim()
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }

        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Сохраняем данные пользователя в Firestore
                    saveUserDataToFirestore(
                        email,
                        firstName.text.toString().trim(),
                        lastName.text.toString().trim(),
                        middleName.text.toString().trim(),
                        group.text.toString().trim()
                    )

                    // Сохраняем все данные в SharedPreferences
                    saveUserDataToSharedPref(
                        email = email,
                        firstName = firstName.text.toString().trim(),
                        lastName = lastName.text.toString().trim(),
                        middleName = middleName.text.toString().trim(),
                        group = group.text.toString().trim()
                    )

                    Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Ошибка регистрации: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun saveUserDataToFirestore(
        email: String,
        firstName: String,
        lastName: String,
        middleName: String,
        group: String
    ) {
        val user = hashMapOf(
            "email" to email,
            "firstName" to firstName,
            "lastName" to lastName,
            "middleName" to middleName,
            "group" to group,
            "about" to ""
        )

        db.collection("users").document(email)
            .set(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Данные пользователя сохранены")
            }
            .addOnFailureListener { e ->
                Log.w("RegisterActivity", "Ошибка сохранения данных", e)
            }
    }

    private fun saveUserDataToSharedPref(
        email: String,
        firstName: String,
        lastName: String,
        middleName: String,
        group: String
    ) {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_email", email)
            putString("first_name", firstName)
            putString("last_name", lastName)
            putString("middle_name", middleName)
            putString("group", group)
            // Инициализируем статистику
            putInt("levels_completed", 0)
            putInt("current_level", 1)
            putString("current_world", "Программирование на C#")
            putInt("rank", 0)
            apply()
        }
    }

    //Проверка на формат группы
    private fun IsValidGroup(group: String): Boolean {
        val regex = "^[А-Я]{2,6}-\\d{2}(?:-\\d)?$".toRegex()
        return regex.matches(group)
    }

    //Проверка на пустые поля
    private fun isNullFields(
        context: Context,
        editText1: EditText,
        editText2: EditText,
        editText3: EditText,
        editText4: EditText,
        editText5: EditText,
        editText6: EditText
    ): Boolean {
        if (editText1.text.isEmpty() || editText2.text.isEmpty() ||
            editText3.text.isEmpty() || editText4.text.isEmpty() ||
            editText5.text.isEmpty() || editText6.text.isEmpty()
        ) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    //Проверка на корректность формата Email
    private fun IsValidEmail(context: Context, EmailEditText: EditText): Boolean {
        val email = EmailEditText.text.toString()

        if (email.isEmpty()) { //чтобы при пустом поле не срабатывал
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailEditText.error = "Некорректный формат email"
            return false
        }
        return true
    }

    private fun checkUserExists(email: String, callback: (Boolean) -> Unit) {
        db.collection("users").document(email)
            .get()
            .addOnSuccessListener { document ->
                callback(document.exists())
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}
