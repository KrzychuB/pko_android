package pl.kb.movie.domain.repositories

import kotlinx.coroutines.flow.Flow
import pl.kb.movie.data.NetworkResult
import pl.kb.movie.data.models.MovieByIdResponse
import pl.kb.movie.data.models.MovieResponse
import pl.kb.movie.data.models.NowPlayingMoviesResponse

interface MoviesRepository {
    suspend fun fetchMovies(): Flow<NetworkResult<NowPlayingMoviesResponse>>
    suspend fun getFavouriteMovies(): Flow<List<Int>>
}