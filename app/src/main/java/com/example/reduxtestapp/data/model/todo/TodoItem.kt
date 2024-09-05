package com.example.reduxtestapp.data.model.todo

import kotlinx.serialization.Serializable

@Serializable
data class TodoItem (
    val text: String,
    val isCompleted: Boolean
)