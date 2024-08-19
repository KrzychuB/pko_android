package pl.kb.movie.presentation.contracts

import pl.kb.movie.domain.mappers.NowPlayingMoviesDataModel

data class MoviesContractState(
    val nowPlayingMovies: NowPlayingMoviesDataModel? = null,
    val favouriteMovies: List<Int> = listOf(),
    val isLoading: Boolean = false
)