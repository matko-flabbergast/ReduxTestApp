package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.data.model.todo.TodoDto
import com.example.reduxtestapp.data.repository.todo.TodoRepository
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import org.reduxkotlin.Store

class TodoMiddleware (
    private val repo: TodoRepository,
) : Middleware() {

    private fun launchAsyncAndDispatchUpdate(store: Store<AppState>, newListGetter: suspend () -> List<TodoDto>) {
        store.dispatch(Action.Async{
            val newList = newListGetter()
            store.dispatch(Action.Todo.UpdateTodoList(newList))
        })
    }

    override fun middleware(store: Store<AppState>, action: Any) {
        when (action) {
            is Action.Todo.AddTodo -> {
                launchAsyncAndDispatchUpdate(store){
                    repo.insertTodo(
                        TodoDto(action.text, false)
                    )
                }
            }
            is Action.Todo.ToggleTodo -> {
                launchAsyncAndDispatchUpdate(store){
                    repo.toggleTodo(action.index)
                }
            }
            is Action.Todo.FetchTodos -> {
                launchAsyncAndDispatchUpdate(store){
                    repo.getTodos()
                }
            }
            is Action.Todo.EditTodo -> {
                launchAsyncAndDispatchUpdate(store){
                    repo.editTodo(action.index, action.text)
                }
            }
            is Action.Todo.RemoveTodo -> {
                launchAsyncAndDispatchUpdate(store){
                    repo.deleteTodo(action.index)
                }
            }
        }
    }

}