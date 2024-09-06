package com.example.reduxtestapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reduxtestapp.ui.NavGraphs
import com.example.reduxtestapp.ui.destinations.CountryScreenDestination
import com.example.reduxtestapp.ui.destinations.TodoScreenDestination
import com.example.reduxtestapp.ui.home.transitions.HomeTransitions
import com.example.reduxtestapp.ui.theme.ReduxTestAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination(style = HomeTransitions::class)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier) {
    Scaffold (
        modifier = modifier
    ){ padding ->
        TitleSection(
            onNavigateToTodos = { navigator.navigate(
                TodoScreenDestination
            )},
            onNavigateToCountries = { navigator.navigate(
                CountryScreenDestination
            )},
            modifier = Modifier.padding(padding)
        )
    }
}
@Composable
private fun TitleSection(
    onNavigateToTodos: () -> Unit,
    onNavigateToCountries: () -> Unit,
    modifier: Modifier = Modifier) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "TodoApp",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 48.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        Column {
            Button(
                onClick = onNavigateToTodos,
                modifier = Modifier.width(150.dp)
            ) {
                Text("Todos")
            }
            Button(
                onClick = onNavigateToCountries,
                modifier = Modifier.width(150.dp)
            ) {
                Text("Countries")
            }
        }

    }
}


@Preview
@Composable
fun HomePreview() {
    ReduxTestAppTheme {
        DestinationsNavHost(navGraph = NavGraphs.root)
    }
}