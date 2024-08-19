package pl.kb.movie.data.models

import com.google.gson.annotations.SerializedName

data class MovieDetailsData(
    @SerializedName("movie_id")
    val movieId: Int,
)