package com.example.reduxtestapp.data.repository.todo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.reduxtestapp.data.model.todo.TodoDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val TODO_NAME = "todos"
val TODO_PREFERENCES_KEY = stringPreferencesKey("todo_preferences_key")

data class TodoRepositoryImpl (
    private val context: Context
) : TodoRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = TODO_NAME)
    override suspend fun getTodos() =
        context.dataStore.data.map { preferences ->
            Json.decodeFromString<List<TodoDto>>(
                preferences[TODO_PREFERENCES_KEY] ?: "[]"
            )
        }.first()

    override suspend fun insertTodo(todoDto: TodoDto): List<TodoDto> {
        var newList: List<TodoDto>? = null
        context.dataStore.edit { preferences ->
            val list = Json.decodeFromString<List<TodoDto>>(
                preferences[TODO_PREFERENCES_KEY] ?: "[]"
            ).toMutableList().apply { add(todoDto) }
            preferences[TODO_PREFERENCES_KEY] = Json.encodeToString(list)
            newList = list
        }
        return newList ?: listOf()
    }

    override suspend fun toggleTodo(index: Int): List<TodoDto> {
        var newList: List<TodoDto>? = null
        context.dataStore.edit { preferences ->
            val list = Json.decodeFromString<List<TodoDto>>(
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
