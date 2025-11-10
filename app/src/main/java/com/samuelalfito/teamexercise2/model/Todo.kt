package com.samuelalfito.teamexercise2.model

data class Todo(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)