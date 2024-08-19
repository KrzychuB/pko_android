package pl.kb.movie.di

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pl.kb.movie.BuildConfig
import pl.kb.movie.data.database.MoviesDatabase
import pl.kb.movie.data.repositories.MovieDetailsRepositoryImpl
import pl.kb.movie.data.repositories.MoviesRepositoryImpl
import pl.kb.movie.data.services.MovieDatabaseService
import pl.kb.movie.data.services.MovieDatabaseServiceImpl
import pl.kb.movie.data.services.MoviesService
import pl.kb.movie.domain.repositories.MovieDetailsRepository
import pl.kb.movie.domain.repositories.MoviesRepository
import pl.kb.movie.domain.usecase.movieDetails.AddFavouriteMovieUseCase
import pl.kb.movie.domain.usecase.movieDetails.AddFavouriteMovieUseCaseImpl
import pl.kb.movie.domain.usecase.movieDetails.CheckFavouriteUseCase
import pl.kb.movie.domain.usecase.movieDetails.CheckFavouriteUseCaseImpl
import pl.kb.movie.domain.usecase.movieDetails.DeleteFavouriteMovieUseCase
import pl.kb.movie.domain.usecase.movieDetails.DeleteFavouriteMovieUseCaseImpl
import pl.kb.movie.domain.usecase.movieDetails.GetMovieByIDUseCase
import pl.kb.movie.domain.usecase.movieDetails.GetMovieByIDUseCaseImpl
import pl.kb.movie.domain.usecase.movies.GetFavouriteMoviesUseCase
import pl.kb.movie.domain.usecase.movies.GetFavouriteMoviesUseCaseImpl
import pl.kb.movie.domain.usecase.movies.GetMoviesUseCase
import pl.kb.movie.domain.usecase.movies.GetMoviesUseCaseImpl
import pl.kb.movie.network.interceptor.HeaderInterceptor
import pl.kb.movie.network.interceptor.NetworkConnectionInterceptor
import pl.kb.movie.presentation.ui.movieDetails.MovieDetailsViewModel
import pl.kb.movie.presentation.ui.movies.MoviesViewModel
import pl.kb.movie.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

private val viewModelModule = module {
    viewModel { MoviesViewModel(get(), get(), get(), get()) }
    viewModel { MovieDetailsViewModel(get(), get(), get(), get()) }
}

private val gsonModule = module {
    single { GsonBuilder().create() }
}

const val url = "MoviesUrl"
private val serviceModule = module {
    single { provideOkHttpClient(androidContext()) }
    single(named(url)) {
        provideCustomRetrofit(
            androidContext(), Constants.baseUrl
        )
    }
    single { get<Retrofit>(named(url)).create(MoviesService::class.java) }
}

private fun provideCustomRetrofit(context: Context, url: String): Retrofit =
    Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
        //.addConverterFactory(nullOnEmptyConverterFactory)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(url)
        .client(provideOkHttpClient(context))
        .build()

private fun provideOkHttpClient(context: Context): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
        }
    return OkHttpClient.Builder().addInterceptor(HeaderInterceptor())
        .addInterceptor(NetworkConnectionInterceptor(context))
        .addInterceptor(loggingInterceptor)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}

private val useCaseModule = module {
    factory<GetMoviesUseCase> { GetMoviesUseCaseImpl(get()) }
    factory<GetMovieByIDUseCase> { GetMovieByIDUseCaseImpl(get()) }
    factory<CheckFavouriteUseCase> { CheckFavouriteUseCaseImpl(get()) }
    factory<AddFavouriteMovieUseCase> { AddFavouriteMovieUseCaseImpl(get()) }
    factory<DeleteFavouriteMovieUseCase> { DeleteFavouriteMovieUseCaseImpl(get()) }
    factory<GetFavouriteMoviesUseCase> { GetFavouriteMoviesUseCaseImpl(get()) }
}

private val repoModule = module {
    single<MoviesRepository> { MoviesRepositoryImpl(get(), get()) }
    single<MovieDetailsRepository> { MovieDetailsRepositoryImpl(get(), get()) }
}

private val databaseModule = module {
    single { MoviesDatabase.getInstance(androidContext()) }
    single { get<MoviesDatabase>().favouriteMoviesDao() }
    single<MovieDatabaseService> { MovieDatabaseServiceImpl(get()) }
}

val allModules = listOf(
    viewModelModule,
    gsonModule,
    serviceModule,
    repoModule,
    useCaseModule,
    databaseModule
)