package pl.kb.movie.domain.usecase.movieDetails

import kotlinx.coroutines.flow.Flow
import pl.kb.movie.data.NetworkResult
import pl.kb.movie.domain.mappers.MovieByIdDataModel

interface GetMovieByIDUseCase {
    suspend fun execute(movieId: Int): Flow<NetworkResult<MovieByIdDataModel>>
}