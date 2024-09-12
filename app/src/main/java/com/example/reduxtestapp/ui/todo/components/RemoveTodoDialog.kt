package com.example.reduxtestapp.ui.todo.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
fun RemoveTodoDialog(
    index: Int,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    store: Store<AppState> = koinInject(),
) {
    AlertDialog(
        onDismissRequest = { navigator.popBackStack() },
        title = { Text(stringResource(R.string.remove_todo)) },
        text = {
            Text(stringResource(R.string.remove_todo_dialog_text))
        },
        modifier = modifier,
        confirmButton = {
            TextButton(
                onClick = {
                    store.dispatch(Action.Todo.RemoveTodo(index))
                    navigator.popBackStack()
                }
            ) {
                Text(stringResource(R.string.remove_todo_dialog_affirmative))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    navigator.popBackStack()
                }
            ) {
                Text(stringResource(R.string.remove_todo_dialog_negative))
            }
        }

    )
}