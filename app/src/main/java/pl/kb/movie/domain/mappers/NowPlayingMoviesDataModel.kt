package pl.kb.movie.domain.mappers

data class NowPlayingMoviesDataModel(
    val dates: DatesDataModel,
    val page: Int,
    val results: List<MovieDataModel>,
    val totalPages: Int,
    val totalResults: Int,
)