package pl.kb.movie.data.models

import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
)