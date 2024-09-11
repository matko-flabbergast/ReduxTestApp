package com.example.reduxtestapp.ui.todo

import com.example.reduxtestapp.domain.model.todo.TodoModel
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

fun TodoModel.asPresentation() = TodoItem(
    text = text,
    isCompleted = isCompleted
)

fun List<TodoModel>.asPresentation(): List<TodoItem> = map { it.asPresentation() }