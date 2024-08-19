package pl.kb.movie.domain.usecase.movieDetails

import kotlinx.coroutines.flow.flow
import pl.kb.movie.data.models.MovieDetailsData
import pl.kb.movie.domain.repositories.MovieDetailsRepository

class AddFavouriteMovieUseCaseImpl(private val movieDetailsRepository: MovieDetailsRepository) :
    AddFavouriteMovieUseCase {
    override suspend fun execute(movieId: Int) = flow<Unit> {
        MovieDetailsData(movieId).let {
            movieDetailsRepository.addFavouriteMovie(it).collect{
                emit(Unit)
            }
        }
    }
}