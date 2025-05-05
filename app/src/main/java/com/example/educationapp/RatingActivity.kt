package com.example.educationapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.educationapp.R

class RatingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RatingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        recyclerView = findViewById(R.id.rating_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dummyUsers = listOf(
            RatingItem("Имя пользователя 1"),
            RatingItem("Имя пользователя 2"),
            RatingItem("Имя пользователя 3"),
            RatingItem("Имя пользователя 4"),
            RatingItem("Имя пользователя 5"),
            RatingItem("Имя пользователя 6"),
            RatingItem("Имя пользователя 7"),
            RatingItem("Имя пользователя 8"),
            RatingItem("Имя пользователя 9")
        )

        adapter = RatingAdapter(dummyUsers)
        recyclerView.adapter = adapter
    }
}
