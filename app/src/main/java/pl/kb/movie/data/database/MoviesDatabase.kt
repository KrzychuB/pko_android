package pl.kb.movie.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.kb.movie.data.database.dao.FavouriteMoviesDao
import pl.kb.movie.data.database.entities.FavouriteMoviesEntity
import pl.kb.movie.utils.Constants.Companion.DATABASE_NAME

@Database(
    entities = [
        FavouriteMoviesEntity::class
    ],
    version = 1,
    exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun favouriteMoviesDao(): FavouriteMoviesDao
    companion object {
        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        fun getInstance(context: Context): MoviesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): MoviesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}