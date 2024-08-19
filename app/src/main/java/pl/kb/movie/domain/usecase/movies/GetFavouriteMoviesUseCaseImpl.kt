package pl.kb.movie.domain.usecase.movies

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.kb.movie.domain.repositories.MoviesRepository

class GetFavouriteMoviesUseCaseImpl(private val moviesRepository: MoviesRepository):
    GetFavouriteMoviesUseCase {
    override suspend fun execute(): Flow<List<Int>> = flow {
        moviesRepository.getFavouriteMovies().collect {
            emit(it)
        }
    }
}