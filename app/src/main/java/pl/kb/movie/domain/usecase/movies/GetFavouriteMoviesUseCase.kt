package pl.kb.movie.domain.usecase.movies

import kotlinx.coroutines.flow.Flow

interface GetFavouriteMoviesUseCase {
    suspend fun execute(): Flow<List<Int>>
}