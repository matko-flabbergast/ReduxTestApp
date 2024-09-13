package com.example.reduxtestapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.ui.home.transitions.HomeTransitions
import com.example.reduxtestapp.ui.theme.ReduxTestAppTheme
import com.example.reduxtestapp.util.OnLifecycleChange
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@RootNavGraph(start = true)
@Destination(style = HomeTransitions::class)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    OnLifecycleChange { event ->
        when(event) {
            Lifecycle.Event.ON_START -> viewModel.dispatch(Action.Home.PollPrice)
            Lifecycle.Event.ON_STOP -> viewModel.dispatch(Action.Home.StopPollingPrice)
            else -> {}
        }
    }

    HomeContent(
        uiState = uiState
    )

}

@Composable
private fun HomeContent(
    uiState: HomeViewState,
    modifier: Modifier = Modifier
){
    Scaffold { padding ->
        PriceComponent(
            price = uiState.price,
            lastUpdated = uiState.lastUpdated,
            modifier = modifier.padding(padding))
    }
}

@Composable
private fun PriceComponent(
    price: String,
    lastUpdated: String,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = price,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 48.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "last fetch: $lastUpdated"
        )
    }


}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val mockUiState = HomeViewState()
    ReduxTestAppTheme {
        HomeContent(mockUiState,Modifier)
    }
}