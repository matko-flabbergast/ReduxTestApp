package com.example.reduxtestapp.ui.country

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.reduxtestapp.R
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.state.CountryState
import com.example.reduxtestapp.ui.theme.ReduxTestAppTheme
import com.example.reduxtestapp.util.collectState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import org.reduxkotlin.Store

@RootNavGraph
@Destination
@Composable
fun CountryScreen(
    store: Store<AppState> = koinInject(),
) {

    val uiState = store.collectState(AppState::toCountryViewState)

    CountryContent(
        uiState = uiState,
        onSendSearchQuery = {
            store.dispatch(Action.Country.SearchCountries(it))
        },
        onErrorClick = {
            store.dispatch(Action.Country.SearchCountries(it))
        }
    )
}



@Composable
private fun CountryContent(
    uiState: CountryViewState,
    modifier: Modifier = Modifier,
    onSendSearchQuery: (String) -> Unit,
    onErrorClick: (String) -> Unit,
) {

    Scaffold (modifier = modifier){ padding ->
        Column (
            modifier = modifier.padding(padding)
        ){
            var searchQuery by remember {
                mutableStateOf("")
            }
            SearchBar(
                searchQuery = searchQuery,
                onSendSearchQuery = onSendSearchQuery,
                onSearchChanged = {
                    searchQuery = it
                },
                modifier = Modifier.padding(16.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxSize()
            ){
                when (uiState.status) {
                    CountryState.Status.PENDING -> {
                        CircularProgressIndicator()
                    }
                    CountryState.Status.SUCCESS -> {
                        CountryList(
                            countryItems = uiState.countryList,
                        )
                    }
                    CountryState.Status.ERROR -> {
                        ErrorButton(
                            modifier = Modifier.fillMaxSize(),
                            onClick = {
                                onErrorClick(searchQuery)
                            }
                        )
                    }
                }
            }

        }

    }
}
@Composable
private fun CountryList(
    countryItems: List<CountryItem>,
    modifier: Modifier = Modifier
) {
    if (countryItems.isNotEmpty()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.fillMaxSize()

        ) {
            items(countryItems) { item ->
                CountryItem(item)
            }
        }
    } else {
        Text(
            text = stringResource(R.string.no_countries_found),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun CountryItem(
    countryData: CountryItem,
    modifier: Modifier = Modifier
) {

    Column (
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ){
        Text(countryData.name)
        Text(countryData.languages.toString())
    }

}
@Composable
private fun ErrorButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            stringResource(R.string.error_occurred),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onClick
        ) {
            Text(stringResource(R.string.error_try_again))
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    onSendSearchQuery: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = searchQuery) {
        delay(1000L)
        onSendSearchQuery(searchQuery)
    }

    TextField(
        value = searchQuery,
        onValueChange = onSearchChanged,
        placeholder = {
            Text(stringResource(R.string.search_placeholder))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Preview (group = "Screen")
@Composable
private fun CountryPreviewSuccess() {
    ReduxTestAppTheme {
        val mockUiState = CountryViewState(
            countryList = listOf(
                CountryItem(
                    "Croatia",
                    hashMapOf("hrv" to "Hrvatski")
                ),
                CountryItem(
                        "England",
                hashMapOf("eng" to "English")
            )
            ),
            status = CountryState.Status.SUCCESS

        )
        CountryContent(
            uiState = mockUiState,
            onSendSearchQuery = {},
            onErrorClick = {}
        )
    }
}


