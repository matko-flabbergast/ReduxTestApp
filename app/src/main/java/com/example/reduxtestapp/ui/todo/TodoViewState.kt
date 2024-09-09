package com.example.reduxtestapp.ui.todo

import com.example.reduxtestapp.data.model.todo.TodoDto
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.state.TodoState

data class TodoViewState (
    val todoList: List<TodoItem> = listOf(),
    val status: TodoState.Status = TodoState.Status.SUCCESS
)

fun AppState.toTodoViewState() = TodoViewState(
    todoList = todoState.todoList.asPresentation(),
    status = todoState.status
)

data class TodoItem (
    val text: String,
    val isCompleted: Boolean
)

fun TodoDto.asPresentation() = TodoItem(
    text = text,
    isCompleted = isCompleted
)

fun List<TodoDto>.asPresentation(): List<TodoItem> = map { it.asPresentation() }