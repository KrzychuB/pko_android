package pl.kb.movie.domain.repositories

import kotlinx.coroutines.flow.Flow
import pl.kb.movie.data.NetworkResult
import pl.kb.movie.data.models.MovieByIdResponse
import pl.kb.movie.data.models.MovieDetailsData

interface MovieDetailsRepository {
    suspend fun fetchMovieByID(movieId: Int): Flow<NetworkResult<MovieByIdResponse>>
    suspend fun addFavouriteMovie(favouriteMovie: MovieDetailsData): Flow<Unit>
    suspend fun deleteFavouriteMovie(favouriteMovie: MovieDetailsData): Flow<Unit>
    suspend fun isFavourite(movieId: Int): Flow<Int?>
}