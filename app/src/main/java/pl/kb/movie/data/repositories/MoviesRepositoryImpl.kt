package pl.kb.movie.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pl.kb.movie.data.NetworkResult
import pl.kb.movie.data.models.NowPlayingMoviesResponse
import pl.kb.movie.data.services.MovieDatabaseService
import pl.kb.movie.data.services.MoviesService
import pl.kb.movie.domain.repositories.MoviesRepository

class MoviesRepositoryImpl(
    private val moviesService: MoviesService,
    private val moviesDatabaseService: MovieDatabaseService,
) : MoviesRepository {
    override suspend fun fetchMovies() = flow<NetworkResult<NowPlayingMoviesResponse>> {
        emit(NetworkResult.Loading())
        with(moviesService.getMovies()) {
            if (isSuccessful) {
                emit(NetworkResult.Success(this.body()))
            } else {
                emit(NetworkResult.Error(this.errorBody().toString()))
            }
        }
    }.catch {
        emit(NetworkResult.Error(it.localizedMessage))
    }

    override suspend fun getFavouriteMovies(): Flow<List<Int>> =
        flow {
            emit(moviesDatabaseService.getFavouriteMovies())
        }
}