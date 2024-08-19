package pl.kb.movie.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.kb.movie.presentation.ui.movieDetails.MoviesDetails
import pl.kb.movie.presentation.ui.movies.MoviesScreen
import pl.kb.movie.presentation.ui.unexpectedError.UnexpectedErrorScreen

@Composable
fun MovieNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MovieNavigationActions.HOME_ROUTE,
    movieNavigationActions: MovieNavigationActions
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = MovieNavigationActions.HOME_ROUTE,
        ) {
            MoviesScreen(
                onItemClicked = { movieId ->
                    movieNavigationActions.navigateToMovieDetails(movieId)
                },
            )
        }
        composable(
            "${MovieNavigationActions.MOVIE_DETAILS_ROUTE}/{${MovieNavigationActions.MOVIE_DETAILS_ARG_ROUTE}}",
            arguments = listOf(
                navArgument(name = MovieNavigationActions.MOVIE_DETAILS_ARG_ROUTE) {
                    type = NavType.IntType
                }
            ),
        ) { entry ->
            val movieId = entry.arguments?.getInt(MovieNavigationActions.MOVIE_DETAILS_ARG_ROUTE)
            movieId?.run {
                MoviesDetails(
                    movieId = this,
                    onBackPressed = movieNavigationActions.onBackPressed,
                )
            } ?: run {
                UnexpectedErrorScreen(onBackPressed = movieNavigationActions.onBackPressed)
            }
        }
    }
}
