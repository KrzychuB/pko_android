package pl.kb.movie.presentation.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.kb.movie.data.NetworkResult
import pl.kb.movie.domain.usecase.movieDetails.AddFavouriteMovieUseCase
import pl.kb.movie.domain.usecase.movieDetails.DeleteFavouriteMovieUseCase
import pl.kb.movie.domain.usecase.movies.GetFavouriteMoviesUseCase
import pl.kb.movie.domain.usecase.movies.GetMoviesUseCase
import pl.kb.movie.presentation.contracts.BaseContract
import pl.kb.movie.presentation.contracts.MoviesContractState

class MoviesViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val addFavouriteMovieUseCase: AddFavouriteMovieUseCase,
    private val deleteFavouriteMovieUseCase: DeleteFavouriteMovieUseCase,
    private val getFavouriteMoviesUseCase: GetFavouriteMoviesUseCase,
) : ViewModel() {

    init {
        getMoviesData()
    }

    var effects = Channel<BaseContract.Effect>(Channel.UNLIMITED)
        private set

    private val _state = MutableStateFlow(
        MoviesContractState(
            nowPlayingMovies = null,
            isLoading = true
        )
    )
    val state: StateFlow<MoviesContractState> = _state

    private fun updateState(newState: MoviesContractState) {
        _state.value = newState
    }

    fun getMoviesData() {
        viewModelScope.launch(Dispatchers.IO) {
            getMoviesUseCase.execute().collect {
                when (it) {
                    is NetworkResult.Success -> {
                        val newState =
                            state.value.copy(nowPlayingMovies = it.data, isLoading = false)
                        updateState(newState)
                        effects.send(BaseContract.Effect.DataWasLoaded)
                    }

                    is NetworkResult.Error   -> {
                        val newState = state.value.copy(isLoading = false)
                        updateState(newState)
                        effects.send(BaseContract.Effect.Error(it.message))
                    }

                    is NetworkResult.Loading -> {
                        if (!state.value.isLoading) {
                            val newState = state.value.copy(isLoading = true)
                            updateState(newState)
                        }
                    }

                }
            }
        }
    }

    fun isFavouriteCheck(moveId: Int): Boolean {
        val isFavourite = state.value.favouriteMovies.find { id -> id == moveId }
        return isFavourite != null
    }

    fun getFavouriteMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavouriteMoviesUseCase.execute().collect {
                val newState =
                    state.value.copy(favouriteMovies = it)
                updateState(newState)
                effects.send(BaseContract.Effect.DataWasLoaded)
            }
        }
    }

    fun favouriteClicked(isFavourite: Boolean, movieId: Int) {
        if (isFavourite) {
            addFavouriteMovieData(movieId)
        } else {
            deleteFavouriteMovieData(movieId)
        }
    }

    private fun addFavouriteMovieData(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            addFavouriteMovieUseCase.execute(movieId).collect {
                getFavouriteMovies()
            }
        }
    }

    private fun deleteFavouriteMovieData(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFavouriteMovieUseCase.execute(movieId).collect {
                getFavouriteMovies()
            }
        }
    }
}