package pl.kb.movie.data.models

import com.google.gson.annotations.SerializedName

data class NowPlayingMoviesResponse(
    @SerializedName("dates")
    val dates: DatesResponse,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieResponse>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
)
