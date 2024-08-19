package pl.kb.movie.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
data class FavouriteMoviesEntity(
    @PrimaryKey
    val movieId: Int,
)