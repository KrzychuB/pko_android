package pl.kb.movie.domain.usecase.movieDetails

import kotlinx.coroutines.flow.Flow

interface AddFavouriteMovieUseCase {
    suspend fun execute(movieId: Int): Flow<Unit>
}