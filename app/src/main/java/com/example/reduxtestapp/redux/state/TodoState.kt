package com.example.reduxtestapp.redux.state

import com.example.reduxtestapp.data.model.todo.TodoDto

data class TodoState (
    val todoList: List<TodoDto> = listOf(),
    val status: Status = Status.PENDING,
) {
    enum class Status { SUCCESS, PENDING, ERROR }
}