package com.example.reduxtestapp.data.repository.todo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.reduxtestapp.redux.VisibilityFilter
import com.example.reduxtestapp.data.model.todo.TodoItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val TODO_NAME = "todos"
val TODO_PREFERENCES_KEY = stringPreferencesKey("todo_preferences_key")

data class TodoRepositoryImplementation (
    private val context: Context
) : TodoRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = TODO_NAME)
    override suspend fun getTodos(visibilityFilter: VisibilityFilter) =
        context.dataStore.data.map { preferences ->
            Json.decodeFromString<List<TodoItem>>(
                preferences[TODO_PREFERENCES_KEY] ?: "[]"
            )
        }.first()

    override suspend fun insertTodo(todoItem: TodoItem): List<TodoItem> {
        var newList: List<TodoItem>? = null
        context.dataStore.edit { preferences ->
            val list = Json.decodeFromString<List<TodoItem>>(
                preferences[TODO_PREFERENCES_KEY] ?: "[]"
            ).toMutableList().apply { add(todoItem) }
            preferences[TODO_PREFERENCES_KEY] = Json.encodeToString(list)
            newList = list
        }
        return newList ?: listOf()
    }

    override suspend fun toggleTodo(index: Int): List<TodoItem> {
        var newList: List<TodoItem>? = null
        context.dataStore.edit { preferences ->
            val list = Json.decodeFromString<List<TodoItem>>(
                preferences[TODO_PREFERENCES_KEY] ?: "[]"
            ).toMutableList()
            list[index] = list[index].copy(
                isCompleted = !list[index].isCompleted
            )
            preferences[TODO_PREFERENCES_KEY] = Json.encodeToString(list)
            newList = list
        }
        return newList ?: listOf()
    }

    override suspend fun longNetworkCall(): String {
        val delayTime = 2000L
        delay(delayTime)
        return "Result after ${delayTime}ms"
    }

}
