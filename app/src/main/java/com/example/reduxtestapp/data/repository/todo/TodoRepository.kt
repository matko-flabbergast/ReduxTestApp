package com.example.reduxtestapp.data.repository.todo

import com.example.reduxtestapp.redux.VisibilityFilter
import com.example.reduxtestapp.data.model.todo.TodoItem

interface TodoRepository {

    suspend fun getTodos(
        visibilityFilter: VisibilityFilter = VisibilityFilter.ALL
    ): List<TodoItem>

    suspend fun insertTodo(
        todoItem: TodoItem
    ): List<TodoItem>

    suspend fun toggleTodo(
        index: Int
    ): List<TodoItem>

    suspend fun longNetworkCall(): String
}