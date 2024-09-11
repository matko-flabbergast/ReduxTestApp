package com.example.reduxtestapp.domain.model.todo

import kotlinx.serialization.Serializable

@Serializable
data class TodoModel (
    val text: String,
    val isCompleted: Boolean
)