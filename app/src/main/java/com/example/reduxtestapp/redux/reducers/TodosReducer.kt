package com.example.reduxtestapp.redux.reducers

import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.state.TodoState

fun todosReducer(state: AppState, action: Any): TodoState {
    return when (action) {
        is Action.Todo.UpdateTodoList -> {
            state.todoState.copy(
                todoList = action.items,
                status = TodoState.Status.SUCCESS
            )
        }
        is Action.Todo.Error -> {
            state.todoState.copy(
                status = TodoState.Status.ERROR
            )
        }
        is Action.Todo.AddTodo -> {
            state.todoState.copy(
                status = TodoState.Status.PENDING
            )
        }
        is Action.Todo.ToggleTodo -> {
            state.todoState.copy(
                status = TodoState.Status.PENDING
            )
        }
        is Action.Todo.FetchTodos -> {
            state.todoState.copy(
                status = TodoState.Status.PENDING
            )
        }
        else -> state.todoState
    }
}
