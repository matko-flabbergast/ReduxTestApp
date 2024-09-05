
package com.example.reduxtestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.reduxtestapp.ui.NavGraphs
import com.example.reduxtestapp.ui.theme.ReduxTestAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
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
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}
