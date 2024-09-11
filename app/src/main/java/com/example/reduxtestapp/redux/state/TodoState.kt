package com.example.reduxtestapp.redux.state

import com.example.reduxtestapp.domain.model.todo.TodoModel

data class TodoState (
    val todoList: List<TodoModel> = listOf(),
    val status: Status = Status.PENDING,
) {
    enum class Status { SUCCESS, PENDING, ERROR }
}