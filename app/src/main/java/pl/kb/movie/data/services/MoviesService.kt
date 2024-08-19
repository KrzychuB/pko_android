package pl.kb.movie.data.services

import pl.kb.movie.data.models.MovieByIdResponse
import pl.kb.movie.data.models.NowPlayingMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {
    @GET("3/movie/now_playing")
    suspend fun getMovies(): Response<NowPlayingMoviesResponse>

    @GET("3/movie/{movie_id}")
    suspend fun getMovieById(@Path("movie_id") movieId: Int): Response<MovieByIdResponse>
}