package pl.kb.movie.data.repositories

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pl.kb.movie.data.NetworkResult
import pl.kb.movie.data.models.MovieByIdResponse
import pl.kb.movie.data.models.MovieDetailsData
import pl.kb.movie.data.services.MovieDatabaseService
import pl.kb.movie.data.services.MoviesService
import pl.kb.movie.domain.repositories.MovieDetailsRepository

class MovieDetailsRepositoryImpl(
    private val moviesService: MoviesService,
    private val moviesDatabaseService: MovieDatabaseService,
) : MovieDetailsRepository {
    override suspend fun fetchMovieByID(movieId: Int) = flow<NetworkResult<MovieByIdResponse>> {
        emit(NetworkResult.Loading())
        with(moviesService.getMovieById(movieId)) {
            if (isSuccessful) {
                emit(NetworkResult.Success(this.body()))
            } else {
                emit(NetworkResult.Error(this.errorBody().toString()))
            }
        }
    }.catch {
        emit(NetworkResult.Error(it.localizedMessage))
    }

    override suspend fun isFavourite(movieId: Int) = flow {
        emit(moviesDatabaseService.isFavourite(movieId))
    }

    override suspend fun addFavouriteMovie(favouriteMovie: MovieDetailsData) =
        flow {
            emit(
                moviesDatabaseService.insertFavouriteMovie(
                    favouriteMovie.movieId
                )
            )
        }

    override suspend fun deleteFavouriteMovie(favouriteMovie: MovieDetailsData) =
        flow {
            emit(
                moviesDatabaseService.deleteFavouriteMovie(
                    favouriteMovie.movieId
                )
            )
        }
}