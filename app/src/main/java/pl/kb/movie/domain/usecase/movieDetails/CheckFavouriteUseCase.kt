package pl.kb.movie.domain.usecase.movieDetails

import kotlinx.coroutines.flow.Flow

interface CheckFavouriteUseCase {
    suspend fun execute(movieId: Int): Flow<Int?>
}