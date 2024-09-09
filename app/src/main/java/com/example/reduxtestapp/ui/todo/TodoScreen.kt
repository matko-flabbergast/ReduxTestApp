package com.example.reduxtestapp.ui.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.TodoState
import com.example.reduxtestapp.ui.destinations.AddTodoDialogDestination
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
        mutableStateOf(TodoViewState(
            status = TodoState.Status.PENDING
        ))
    }

    store.subscribe {
        uiState = store.state.toTodoViewState()
    }

    // initial fetch of todos
    LaunchedEffect(Unit) {
        store.dispatch(Action.Todo.FetchTodos)
    }

    TodoContent(
        uiState = uiState,
        modifier = modifier,
        onToggleTodo = { index ->
            store.dispatch(Action.Todo.ToggleTodo(index))
        },
        onAddTodoButtonClicked = {
            navigator.navigate(AddTodoDialogDestination)
        }
    )




}

@Composable
private fun TodoContent(
    uiState: TodoViewState,
    modifier: Modifier = Modifier,
    onToggleTodo: (Int) -> Unit,
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
                        onCompleteChanged = { index, _ ->
                            onToggleTodo(index)
                        }
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
    todoItems: List<TodoUiData>,
    onCompleteChanged: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
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

@Preview
@Composable
private fun TodoContentPreview() {
    val mockUiState = TodoViewState(
        listOf(
            TodoUiData("Todo 1", false),
            TodoUiData("Todo 2", true),
            TodoUiData("Todo 3", false),
        ),
        status = TodoState.Status.SUCCESS
    )
    TodoContent(mockUiState, Modifier, {}, {})
    
}
