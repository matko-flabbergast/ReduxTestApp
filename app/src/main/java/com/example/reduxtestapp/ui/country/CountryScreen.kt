package com.example.reduxtestapp.ui.country

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.reduxtestapp.data.model.country.asPresentation
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.ui.home.transitions.HomeTransitions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import org.koin.compose.koinInject
import org.reduxkotlin.Store

@RootNavGraph
@Destination(style = HomeTransitions::class)
@Composable
fun CountryScreen(
    modifier: Modifier = Modifier,
    store: Store<AppState> = koinInject(),
) {

    var uiState by remember {
        mutableStateOf(store.state.countryList.asPresentation())
    }

    var isCountriesLoading by remember {
        mutableStateOf(false)
    }

    var isError by remember {
        mutableStateOf(false)
    }

    store.subscribe {
        uiState = store.state.countryList.asPresentation()
        isCountriesLoading = false
        isError = store.state.isError
    }

    store.dispatch(Action.Country.GetCountries).also { isCountriesLoading = true }

    Scaffold (modifier = modifier){ padding ->
        Column{
            Text("Title")
            if (!isError) {
                CountryList(
                    countryItems = uiState,
                    isLoading = isCountriesLoading,
                    modifier = Modifier.padding(padding)
                )
            } else {
                ErrorButton(
                    modifier = Modifier.fillMaxSize(),
                    onClick = {
                        store.dispatch(Action.Country.GetCountries).also { isCountriesLoading = true }
                    }
                )
            }


        }

    }



}
@Composable
private fun CountryList(
    countryItems: List<CountryUiData>,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ){
        if (!isLoading) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()

            ) {
                items(countryItems) { item ->
                    CountryItem(item)
                }
            }
        } else {
            CircularProgressIndicator()
        }

    }


}
@Composable
private fun CountryItem(
    countryData: CountryUiData,
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
        Text("Error has happened :(", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Button(
            onClick = onClick
        ) {
            Text("Try Again")
        }
    }
}