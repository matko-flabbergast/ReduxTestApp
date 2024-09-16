package com.example.reduxtestapp.ui.todo.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.reduxtestapp.R
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.ui.theme.ReduxTestAppTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import org.koin.compose.koinInject
import org.reduxkotlin.Store

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph
@Destination(style = DestinationStyleBottomSheet::class)
@Composable
fun AddTodoDialog(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    store: Store<AppState> = koinInject(),
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { navigator.popBackStack() }
    ) {
        AddTodoContent(
            onConfirm = { text ->
                store.dispatch(Action.Todo.AddTodo(text))
                navigator.popBackStack()
            }
        )
    }
}

@Composable
fun AddTodoContent(
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier) {

    var text by remember {
        mutableStateOf("")
    }

    Column (
        horizontalAlignment = Alignment.End,
        modifier = Modifier.padding(20.dp).fillMaxSize()
    ){
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text(stringResource(R.string.add_todo_placeholder))
            },
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(
            onClick = {
                onConfirm(text)
            }
        ) {
            Text(stringResource(R.string.add_todo_dialog_affirmative))
        }
    }

}

@Preview (showBackground = true)
@Composable
private fun AddTodoPreview() {
    ReduxTestAppTheme {
        AddTodoContent(
            {}
        )
    }
}