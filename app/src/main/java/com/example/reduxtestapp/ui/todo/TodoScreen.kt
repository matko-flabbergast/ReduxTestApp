package com.example.reduxtestapp.ui.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.data.model.todo.asPresentation
import com.example.reduxtestapp.ui.todo.transitions.TodoTransitions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.compose.koinInject
import org.reduxkotlin.Store

@RootNavGraph
@Destination
@Composable
fun TodoScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    store: Store<AppState> = koinInject(),
    ) {


    var uiState by remember {
        mutableStateOf(store.state.todoList.asPresentation())
    }

    var triggerDialog by remember {
        mutableStateOf(false)
    }

    store.subscribe {
        uiState = store.state.todoList.asPresentation()
    }

    // initial fetch of todos
    store.dispatch(Action.Todo.FetchTodos)

    Scaffold (
        floatingActionButton = {
            AddTodoButton(onAddTodoClicked = {triggerDialog = true})
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                "Todo App",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
            )
            TodoList(
                todoItems = uiState,
                onCompleteChanged = { index, _ ->
                    store.dispatch(Action.Todo.ToggleTodo(index))
                }
            )
        }

    }

    if (triggerDialog) {
        AddTodoDialog(
            onConfirm = {text ->
                store.dispatch(Action.Todo.AddTodo(text))
                triggerDialog = false
            },
            onDismiss = {
                triggerDialog = false
            }
        )
    }


}

@Composable
private fun AddTodoButton(
    onAddTodoClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onAddTodoClicked,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
    }
}


@Composable
private fun TodoList (
    todoItems: List<TodoUiData>,
    onCompleteChanged: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        itemsIndexed(todoItems) { index, todoItem ->
            TodoItem(
                title = todoItem.text,
                onCheckedChange = { onCompleteChanged(index, it)  },
                isCompleted = todoItem.isCompleted)
        }
    }
}


@Composable
private fun TodoItem(
    title: String,
    onCheckedChange: (Boolean) -> Unit,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Surface (
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                title,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .width(200.dp)
            )
            Checkbox(
                checked = isCompleted,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
private fun AddTodoDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember {
        mutableStateOf("")
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add New Todo") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text("Todo Text")
                }
            )
        },
        modifier = modifier,
        confirmButton = {
            TextButton(onClick = {onConfirm(text)}) {
                Text(text = "Add")
            }
        }
    )
}
