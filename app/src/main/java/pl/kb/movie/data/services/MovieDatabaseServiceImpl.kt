package pl.kb.movie.data.services

import pl.kb.movie.data.database.dao.FavouriteMoviesDao
import pl.kb.movie.data.database.entities.FavouriteMoviesEntity

class MovieDatabaseServiceImpl(private val favouriteMoviesDao: FavouriteMoviesDao): MovieDatabaseService {
    override suspend fun insertFavouriteMovie(movieId: Int) {
        return FavouriteMoviesEntity(movieId).let {
            favouriteMoviesDao.insertFavouriteMoviesEntity(it)
        }
    }

    override suspend fun deleteFavouriteMovie(movieId: Int) {
        return favouriteMoviesDao.deleteFavouriteMovie(movieId)
    }

    override suspend fun isFavourite(movieId: Int): Int? {
        return favouriteMoviesDao.getFavouriteMovieId(movieId)
    }

    override suspend fun getFavouriteMovies(): List<Int> {
        return favouriteMoviesDao.getFavouriteMovies()
    }
}