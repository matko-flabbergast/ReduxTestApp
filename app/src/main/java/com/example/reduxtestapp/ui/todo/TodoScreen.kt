package com.example.reduxtestapp.ui.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.state.TodoState
import com.example.reduxtestapp.ui.destinations.AddTodoDialogDestination
import com.example.reduxtestapp.ui.destinations.EditTodoDialogDestination
import com.example.reduxtestapp.ui.destinations.RemoveTodoDialogDestination
import com.example.reduxtestapp.ui.todo.transitions.TodoTransitions
import com.example.reduxtestapp.util.collectState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.compose.koinInject
import org.reduxkotlin.Store

@RootNavGraph
@Destination(style = TodoTransitions::class)
@Composable
fun TodoScreen(
    navigator: DestinationsNavigator,
    store: Store<AppState> = koinInject(),
    ) {


    val uiState = store.collectState(AppState::toTodoViewState)

    // initial fetch of todos
    LaunchedEffect(Unit) {
        store.dispatch(Action.Todo.FetchTodos)
    }

    TodoContent(
        uiState = uiState,
        onToggleTodo = { index ->
            store.dispatch(Action.Todo.ToggleTodo(index))
        },
        onAddTodoButtonClicked = {
            navigator.navigate(AddTodoDialogDestination)
        },
        onEditTodo = { index, todoItem ->
            navigator.navigate(
                EditTodoDialogDestination(
                    index = index,
                    todoText = todoItem.text
                )
            )
        },
        onRemoveTodo = { index ->
            navigator.navigate(
                RemoveTodoDialogDestination(
                    index = index
                )
            )
        }
    )




}

@Composable
private fun TodoContent(
    uiState: TodoViewState,
    modifier: Modifier = Modifier,
    onToggleTodo: (Int) -> Unit,
    onEditTodo: (Int, TodoItem) -> Unit,
    onRemoveTodo: (Int) -> Unit,
    onAddTodoButtonClicked: () -> Unit,
) {

    Scaffold (
        floatingActionButton = {
            AddTodoButton(onAddTodoClicked = onAddTodoButtonClicked)
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState.status) {
                TodoState.Status.SUCCESS -> {
                    TodoList(
                        todoItems = uiState.todoList,
                        modifier = Modifier,
                        onToggleTodo = { index, _ ->
                            onToggleTodo(index)
                        },
                        onEditTodo = onEditTodo,
                        onRemoveTodo = onRemoveTodo
                    )
                }

                TodoState.Status.PENDING -> {
                    CircularProgressIndicator()
                }

                TodoState.Status.ERROR -> {}
            }
        }
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
    todoItems: List<TodoItem>,
    onToggleTodo: (Int, Boolean) -> Unit,
    onEditTodo: (Int, TodoItem) -> Unit,
    onRemoveTodo: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .padding(bottom = 100.dp)
    ) {
        itemsIndexed(todoItems) { index, todoItem ->
            TodoItem(
                title = todoItem.text,
                onCheckedChange = { onToggleTodo(index, it) },
                onEditClicked = { onEditTodo(index, todoItem) },
                onDeleteClicked = { onRemoveTodo(index) },
                isCompleted = todoItem.isCompleted)
        }
    }
}


@Composable
private fun TodoItem(
    title: String,
    onCheckedChange: (Boolean) -> Unit,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
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
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ){
            IconButton(
                onClick = onEditClicked
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = onDeleteClicked
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview
@Composable
private fun TodoContentPreview() {
    val mockUiState = TodoViewState(
        listOf(
            TodoItem("Todo 1", false),
            TodoItem("Todo 2", true),
            TodoItem("Todo 3", false),
        ),
        status = TodoState.Status.SUCCESS
    )
    TodoContent(mockUiState, Modifier, {}, {_, _ ->}, {}, {})
    
}
