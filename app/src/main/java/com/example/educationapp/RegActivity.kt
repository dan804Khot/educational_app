package com.example.educationapp

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
            Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
            val emailText = email.text.toString().trim()
            val firstNameText = firstName.text.toString().trim()
            val lastNameText = lastName.text.toString().trim()
            val middleNameText = middleName.text.toString().trim()
            val groupText = group.text.toString().trim()
            val passwordText = password.text.toString().trim()

            // Регистрация в Firebase
            registerUser(
                emailText,
                passwordText
            )
        }
    }

    private fun registerUser(
        email: String,
        password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Успешная регистрация
                    val user = auth.currentUser
                    Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Ошибка регистрации: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
}
