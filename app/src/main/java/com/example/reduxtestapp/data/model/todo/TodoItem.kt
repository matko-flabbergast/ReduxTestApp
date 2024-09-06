package com.example.reduxtestapp.data.model.todo

import com.example.reduxtestapp.ui.todo.TodoUiData
import kotlinx.serialization.Serializable

@Serializable
data class TodoItem (
    val text: String,
    val isCompleted: Boolean
)

fun TodoItem.asPresentation() = TodoUiData(
    text = text,
    isCompleted = isCompleted
)

fun List<TodoItem>.asPresentation(): List<TodoUiData> = map { it.asPresentation() }
