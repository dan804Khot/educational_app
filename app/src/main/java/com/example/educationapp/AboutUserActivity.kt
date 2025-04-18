package com.example.educationapp

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton

class AboutMeActivity : Activity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var groupEditText: EditText
    private lateinit var aboutTextEditText: EditText
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_user)

        fullNameEditText = findViewById(R.id.full_name)
        groupEditText = findViewById(R.id.group)
        aboutTextEditText = findViewById(R.id.about_text)
        backButton = findViewById(R.id.back_button)

        backButton.setOnClickListener {
            finish()
        }
    }
}
