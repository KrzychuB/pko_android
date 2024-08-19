package pl.kb.movie.domain.usecase.movies

import kotlinx.coroutines.flow.Flow
import pl.kb.movie.data.NetworkResult
import pl.kb.movie.domain.mappers.NowPlayingMoviesDataModel

interface GetMoviesUseCase {
    suspend fun execute(): Flow<NetworkResult<NowPlayingMoviesDataModel>>
}