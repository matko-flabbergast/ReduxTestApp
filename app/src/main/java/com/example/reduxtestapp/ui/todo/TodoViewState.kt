package com.example.reduxtestapp.ui.todo

import com.example.reduxtestapp.data.model.todo.asPresentation
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.TodoState

data class TodoViewState (
    val todoList: List<TodoUiData> = listOf(),
    val status: TodoState.Status = TodoState.Status.SUCCESS
)

fun AppState.toTodoViewState() = TodoViewState(
    todoList = todoState.todoList.asPresentation(),
    status = todoState.status
)

data class TodoUiData (
    val text: String,
    val isCompleted: Boolean
)