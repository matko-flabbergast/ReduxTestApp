package com.example.reduxtestapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TodoItem (
    val text: String,
    val isCompleted: Boolean
)