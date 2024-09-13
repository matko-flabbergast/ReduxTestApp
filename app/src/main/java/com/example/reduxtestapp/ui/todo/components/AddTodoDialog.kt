package com.example.reduxtestapp.ui.todo.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.reduxtestapp.R
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.koin.compose.koinInject
import org.reduxkotlin.Store

@RootNavGraph
@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun AddTodoDialog(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    store: Store<AppState> = koinInject(),
) {
    var text by remember {
        mutableStateOf("")
    }
    AlertDialog(
        onDismissRequest = { navigator.popBackStack() },
        title = { Text(stringResource(R.string.add_todo)) },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(stringResource(R.string.add_todo_placeholder))
                }
            )
        },
        modifier = modifier,
        confirmButton = {
            TextButton(
                onClick = {
                    store.dispatch(Action.Todo.AddTodo(text))
                    navigator.popBackStack()
                }
            ) {
                Text(stringResource(R.string.add_todo_dialog_affirmative))
            }
        }
    )
}