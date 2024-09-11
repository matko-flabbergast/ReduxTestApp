package com.example.reduxtestapp.data.repository.todo

import com.example.reduxtestapp.domain.model.todo.TodoModel

interface TodoRepository {

    suspend fun getTodos(
    ): List<TodoModel>

    suspend fun insertTodo(
        todoModel: TodoModel
    ): List<TodoModel>

    suspend fun toggleTodo(
        index: Int
    ): List<TodoModel>

    suspend fun editTodo(
        index: Int,
        newText: String
    ): List<TodoModel>

    suspend fun deleteTodo(
        index: Int
    ): List<TodoModel>

}