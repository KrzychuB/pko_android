package pl.kb.movie.presentation.contracts

import pl.kb.movie.domain.mappers.MovieByIdDataModel

data class MovieContractState(
    val movieByIdDataModel: MovieByIdDataModel? = null,
    val isLoading: Boolean = false,
    val isFavourite: Boolean = false
)
