package pl.kb.movie.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import pl.kb.movie.presentation.ui.theme.MovieTheme

@Composable
fun MovieApp() {
    MovieTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            MovieNavigationActions(navController)
        }

        Scaffold { innerPadding ->
            MovieNavGraph(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                navController = navController,
                movieNavigationActions = navigationActions,
            )
        }
    }
}