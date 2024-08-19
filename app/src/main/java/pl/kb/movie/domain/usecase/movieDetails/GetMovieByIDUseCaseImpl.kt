package pl.kb.movie.domain.usecase.movieDetails

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.kb.movie.data.NetworkResult
import pl.kb.movie.domain.mappers.MovieByIdDataModel
import pl.kb.movie.domain.mappers.mapMovieByIdDataItems
import pl.kb.movie.domain.repositories.MovieDetailsRepository

class GetMovieByIDUseCaseImpl(private val movieDetailsRepository: MovieDetailsRepository): GetMovieByIDUseCase {
    override suspend fun execute(movieId: Int): Flow<NetworkResult<MovieByIdDataModel>> = flow {
        movieDetailsRepository.fetchMovieByID(movieId).collect { movieByID ->
            when (movieByID) {
                is NetworkResult.Success -> {
                    val movieDataList =
                        movieByID.data?.mapMovieByIdDataItems()
                    emit(NetworkResult.Success(movieDataList))
                }

                is NetworkResult.Error   -> {
                    emit(NetworkResult.Error(movieByID.message))
                }

                is NetworkResult.Loading -> {
                    emit(NetworkResult.Loading())
                }
            }
        }
    }
}