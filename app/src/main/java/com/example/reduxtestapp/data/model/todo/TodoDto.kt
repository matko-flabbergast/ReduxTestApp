package com.example.reduxtestapp.data.model.todo

import kotlinx.serialization.Serializable

@Serializable
data class TodoDto (
    val text: String,
    val isCompleted: Boolean
)

