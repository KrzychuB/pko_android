package pl.kb.movie.data.services

interface MovieDatabaseService {
    suspend fun insertFavouriteMovie(movieId: Int)
    suspend fun deleteFavouriteMovie(movieId: Int)
    suspend fun isFavourite(movieId: Int): Int?
    suspend fun getFavouriteMovies(): List<Int>
}