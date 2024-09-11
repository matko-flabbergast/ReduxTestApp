package com.example.reduxtestapp.data.model.todo

import com.example.reduxtestapp.domain.model.todo.TodoModel
import kotlinx.serialization.Serializable

@Serializable
data class TodoDto (
    val text: String,
    val isCompleted: Boolean
)

fun TodoDto.asDomain() = TodoModel(
    text = text,
    isCompleted = isCompleted
)

fun List<TodoDto>.asDomain() = map { it.asDomain() }

fun TodoModel.asData() = TodoDto(
    text = text,
    isCompleted = isCompleted
)

