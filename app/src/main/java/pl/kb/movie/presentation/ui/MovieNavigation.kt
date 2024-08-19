package pl.kb.movie.presentation.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import pl.kb.movie.BuildConfig

class MovieNavigationActions(navController: NavHostController) {
    companion object {
        const val HOME_ROUTE = "home"
        const val MOVIE_DETAILS_ROUTE = "movieDetails"
        const val MOVIE_DETAILS_ARG_ROUTE = "movieId"
    }

    val navigateToHome: () -> Unit = {
        navController.navigate(HOME_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToMovieDetails: (movieId: Int) -> Unit = { movieId ->
        navController.navigate("${MOVIE_DETAILS_ROUTE}/$movieId") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val onBackPressed: () -> Unit = {
        navController.popBackStack()
    }
}