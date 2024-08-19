package pl.kb.movie.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.kb.movie.data.database.entities.FavouriteMoviesEntity

@Dao
interface FavouriteMoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteMoviesEntity(favouriteMoviesEntity: FavouriteMoviesEntity)

    @Query("SELECT movieId from favourite_movies WHERE movieId=:movieId")
    suspend fun getFavouriteMovieId(movieId: Int?): Int?

    @Query("SELECT movieId from favourite_movies")
    suspend fun getFavouriteMovies(): List<Int>

    @Query("DELETE FROM favourite_movies where movieId=:movieId")
    suspend fun deleteFavouriteMovie(movieId: Int)
}