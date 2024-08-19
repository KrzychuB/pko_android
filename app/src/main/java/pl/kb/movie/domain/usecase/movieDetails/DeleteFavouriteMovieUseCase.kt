package pl.kb.movie.domain.usecase.movieDetails

import kotlinx.coroutines.flow.Flow

interface DeleteFavouriteMovieUseCase {
    suspend fun execute(movieId: Int): Flow<Unit>
}