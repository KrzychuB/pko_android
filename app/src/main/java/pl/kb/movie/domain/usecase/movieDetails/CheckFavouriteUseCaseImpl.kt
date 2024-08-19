package pl.kb.movie.domain.usecase.movieDetails

import kotlinx.coroutines.flow.Flow
import pl.kb.movie.domain.repositories.MovieDetailsRepository

class CheckFavouriteUseCaseImpl(private val movieDetailsRepository: MovieDetailsRepository) :
    CheckFavouriteUseCase {
    override suspend fun execute(movieId: Int): Flow<Int?> =
        movieDetailsRepository.isFavourite(movieId)
}