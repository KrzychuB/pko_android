package pl.kb.movie.domain.usecase.movies

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.kb.movie.data.NetworkResult
import pl.kb.movie.domain.mappers.NowPlayingMoviesDataModel
import pl.kb.movie.domain.mappers.mapNowPlayingMoviesDataItems
import pl.kb.movie.domain.repositories.MoviesRepository

class GetMoviesUseCaseImpl(private val moviesRepository: MoviesRepository) : GetMoviesUseCase {
    override suspend fun execute(): Flow<NetworkResult<NowPlayingMoviesDataModel>> = flow {
        moviesRepository.fetchMovies().collect { nowPlayingMoviesResponse ->
            when (nowPlayingMoviesResponse) {
                is NetworkResult.Success -> {
                    val movieDataList =
                        nowPlayingMoviesResponse.data?.mapNowPlayingMoviesDataItems()
                    emit(NetworkResult.Success(movieDataList))
                }

                is NetworkResult.Error   -> {
                    emit(NetworkResult.Error(nowPlayingMoviesResponse.message))
                }

                is NetworkResult.Loading -> {
                    emit(NetworkResult.Loading())
                }
            }
        }
    }
}