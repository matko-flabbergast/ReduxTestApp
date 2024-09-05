package com.example.reduxtestapp.repositories

import com.example.reduxtestapp.data.model.TodoItem
import com.example.reduxtestapp.data.repository.TodoRepository
import com.example.reduxtestapp.redux.VisibilityFilter
import kotlinx.coroutines.delay

class MockTodoRepository : TodoRepository {

    private val todos = mutableListOf<TodoItem>()
    override suspend fun getTodos(visibilityFilter: VisibilityFilter) = todos

    override suspend fun insertTodo(todoItem: TodoItem) =
        todos.apply { add(todoItem) }

    override suspend fun toggleTodo(index: Int) =
        todos.apply {
            todos[index] = todos[index].copy(
                isCompleted = !todos[index].isCompleted
            )
        }

    override suspend fun longNetworkCall(): String {
        delay(2000L)
        return "Success"
    }

}