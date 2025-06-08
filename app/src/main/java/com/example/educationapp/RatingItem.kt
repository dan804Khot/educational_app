package com.example.educationapp

data class RatingItem(
    val username: String,
    val score: Int = 0,
    val isCurrentUser: Boolean = false
)
