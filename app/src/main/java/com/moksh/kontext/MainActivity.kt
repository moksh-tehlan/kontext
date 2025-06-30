package com.moksh.kontext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.navigation.Graphs
import com.moksh.kontext.presentation.navigation.KontextNavGraph
import com.moksh.kontext.presentation.screens.root.RootViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KontextTheme {
                val rootViewModel: RootViewModel = hiltViewModel()
                val startDestination by rootViewModel.startDestination.collectAsState()
                val shouldNavigateToAuth by rootViewModel.shouldNavigateToAuth.collectAsState()

                if (startDestination != null) {
                    val navController = rememberNavController()

                    // Listen for global auth expiry
                    LaunchedEffect(shouldNavigateToAuth) {
                        if (shouldNavigateToAuth) {
                            navController.navigate(Graphs.AuthGraph) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                    
                    KontextNavGraph(
                        navController = navController,
                        startDestination = startDestination!!
                    )
                } else {
                    // Show loading while determining start destination
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KontextTheme {
        Greeting("Android")
    }
}