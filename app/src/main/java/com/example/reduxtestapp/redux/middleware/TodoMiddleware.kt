package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.data.repository.todo.TodoRepository
import com.example.reduxtestapp.domain.model.todo.TodoModel
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import com.example.reduxtestapp.redux.MiddlewareResult

class TodoMiddleware (
    private val repo: TodoRepository,
) : Middleware() {

    override fun handleAction(state: AppState, action: Any): MiddlewareResult {
        return when (action) {
            is Action.Todo.AddTodo -> handleAddTodo(action.text)
            is Action.Todo.ToggleTodo -> handleToggleTodo(action.index)
            is Action.Todo.FetchTodos -> handleFetchTodos()
            is Action.Todo.EditTodo -> handleEditTodo(action.index, action.text)
            is Action.Todo.RemoveTodo -> handleRemoveTodo(action.index)
            else -> MiddlewareResult.Nothing
        }
    }

    private fun launchAsync(newListGetter: suspend () -> List<TodoModel>): MiddlewareResult {
        return MiddlewareResult.Async{
            val newList = newListGetter()
            MiddlewareResult.ResultAction(Action.Todo.UpdateTodoList(newList))
        }
    }

    private fun handleAddTodo(text: String): MiddlewareResult {
        return launchAsync{
            repo.insertTodo(
                TodoModel(text, false)
            )
        }
    }

    private fun handleToggleTodo(index: Int): MiddlewareResult {
        return launchAsync{
            repo.toggleTodo(index)
        }
    }

    private fun handleFetchTodos(): MiddlewareResult {
        return launchAsync{
            repo.getTodos()
        }
    }

    private fun handleEditTodo(index: Int, text: String): MiddlewareResult {
        return launchAsync{
            repo.editTodo(index, text)
        }
    }

    private fun handleRemoveTodo(index: Int): MiddlewareResult {
        return launchAsync{
            repo.deleteTodo(index)
        }
    }

}