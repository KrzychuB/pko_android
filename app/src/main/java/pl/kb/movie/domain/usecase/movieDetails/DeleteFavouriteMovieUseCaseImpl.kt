package pl.kb.movie.domain.usecase.movieDetails

import kotlinx.coroutines.flow.flow
import pl.kb.movie.data.models.MovieDetailsData
import pl.kb.movie.domain.repositories.MovieDetailsRepository

class DeleteFavouriteMovieUseCaseImpl(private val movieDetailsRepository: MovieDetailsRepository): DeleteFavouriteMovieUseCase {
    override suspend fun execute(movieId: Int) = flow<Unit>  {
        MovieDetailsData(movieId).let {
            movieDetailsRepository.deleteFavouriteMovie(it).collect{
                emit(Unit)
            }
        }
    }
}