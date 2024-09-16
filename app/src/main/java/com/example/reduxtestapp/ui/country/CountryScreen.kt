package com.example.reduxtestapp.ui.country

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.collectAsState
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
import com.example.reduxtestapp.redux.state.CountryState
import com.example.reduxtestapp.ui.theme.ReduxTestAppTheme
import com.example.reduxtestapp.ui.todo.transitions.TodoTransitions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination(style = TodoTransitions::class)
@Composable
fun CountryScreen(
    viewModel: CountryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.dispatch(Action.Country.LoadInitialCountries)
    }

    CountryContent(
        uiState = uiState,
        onSendSearchQuery = { lang, curr ->
            viewModel.dispatch(Action.Country.SearchByLanguageAndCurrency(lang, curr))
        },
        onErrorClick = { lang, curr ->
            viewModel.dispatch(Action.Country.SearchByLanguageAndCurrency(lang, curr))
        }
    )
}



@Composable
private fun CountryContent(
    uiState: CountryViewState,
    modifier: Modifier = Modifier,
    onSendSearchQuery: (String, String) -> Unit,
    onErrorClick: (String, String) -> Unit,
) {

    Scaffold (modifier = modifier){ padding ->
        Column (
            modifier = modifier.padding(padding)
        ){
            var languageQuery by remember {
                mutableStateOf("")
            }

            var currencyQuery by remember {
                mutableStateOf("")
            }

            LaunchedEffect(languageQuery, currencyQuery) {
                if (languageQuery.isNotEmpty() && currencyQuery.isNotEmpty()){
                    delay(1000L)
                    onSendSearchQuery(languageQuery, currencyQuery)
                }
            }
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)

            ){
                SearchBar(
                    searchQuery = languageQuery,
                    onSearchChanged = {
                        languageQuery = it
                    },
                    placeholder = R.string.lang_placeholder,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(16.dp))
                SearchBar(
                    searchQuery = currencyQuery,
                    onSearchChanged = {
                        currencyQuery = it
                    },
                    placeholder = R.string.curr_placeholder,
                    modifier = Modifier.weight(1f)
                )
            }
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
                                onErrorClick(languageQuery, currencyQuery)
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
        Text(countryData.currencies.toString())
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
    @StringRes placeholder: Int,
    modifier: Modifier = Modifier
) {

    TextField(
        value = searchQuery,
        onValueChange = onSearchChanged,
        placeholder = {
            Text(stringResource(placeholder))
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
        modifier = modifier
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
                    hashMapOf("hrv" to "Hrvatski"),
                    hashMapOf("eur" to "eur")
                ),
                CountryItem(
                        "England",
                    hashMapOf("eng" to "English"),
                    hashMapOf("eur" to "eur")
            )
            ),
            status = CountryState.Status.SUCCESS

        )
        CountryContent(
            uiState = mockUiState,
            onSendSearchQuery = {_, _ ->},
            onErrorClick = {_, _ ->}
        )
    }
}


