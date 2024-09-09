
package com.example.reduxtestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.reduxtestapp.ui.NavGraphs
import com.example.reduxtestapp.ui.destinations.CountryScreenDestination
import com.example.reduxtestapp.ui.destinations.HomeScreenDestination
import com.example.reduxtestapp.ui.destinations.TodoScreenDestination
import com.example.reduxtestapp.ui.theme.ReduxTestAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.startDestination
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReduxTestApp()
        }
    }
}

@Composable
fun ReduxTestApp() {
    ReduxTestAppTheme {
        KoinContext {
            val navController = rememberNavController()
            Scaffold (
                bottomBar = { MyBottomNav(navController) }
            ) { padding ->

                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    navController = navController,
                    modifier = Modifier.padding(padding))
            }
        }
    }
}

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Home(HomeScreenDestination, Icons.Default.Home, R.string.home_screen),
    Todo(TodoScreenDestination, Icons.Default.Edit, R.string.todo_screen),
    Country(CountryScreenDestination, Icons.Default.Search, R.string.country_screen)
}

@Composable
private fun MyBottomNav(
    destinationsNavigator: NavController,
    modifier: Modifier = Modifier
) {
    val currentDestination = destinationsNavigator.currentDestinationAsState().value
        ?: NavGraphs.root.startDestination
    NavigationBar (modifier){
        BottomBarDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    destinationsNavigator.navigate(destination.direction) {
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(destination.label))
                }
            )
        }
    }
}
