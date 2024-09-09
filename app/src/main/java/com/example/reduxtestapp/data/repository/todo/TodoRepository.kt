package com.example.reduxtestapp.data.repository.todo

import com.example.reduxtestapp.data.model.todo.TodoDto

interface TodoRepository {

    suspend fun getTodos(
    ): List<TodoDto>

    suspend fun insertTodo(
        todoDto: TodoDto
    ): List<TodoDto>

    suspend fun toggleTodo(
        index: Int
    ): List<TodoDto>

    suspend fun longNetworkCall(): String
}