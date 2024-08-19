package pl.kb.movie.presentation.ui.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pl.kb.movie.data.NetworkResult
import pl.kb.movie.domain.usecase.movieDetails.AddFavouriteMovieUseCase
import pl.kb.movie.domain.usecase.movieDetails.CheckFavouriteUseCase
import pl.kb.movie.domain.usecase.movieDetails.DeleteFavouriteMovieUseCase
import pl.kb.movie.domain.usecase.movieDetails.GetMovieByIDUseCase
import pl.kb.movie.presentation.contracts.BaseContract
import pl.kb.movie.presentation.contracts.MovieContractState

class MovieDetailsViewModel(
    private val getMovieByIDUseCase: GetMovieByIDUseCase,
    private val checkFavouriteUseCase: CheckFavouriteUseCase,
    private val addFavouriteMovieUseCase: AddFavouriteMovieUseCase,
    private val deleteFavouriteMovieUseCase: DeleteFavouriteMovieUseCase,
) : ViewModel() {

    var effects = Channel<BaseContract.Effect>(Channel.UNLIMITED)
        private set

    private val _state =
        MutableStateFlow(MovieContractState(movieByIdDataModel = null, isLoading = true))
    val state: StateFlow<MovieContractState> = _state

    private fun updateState(newState: MovieContractState) {
        _state.value = newState
    }

    fun checkFavourite(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            checkFavouriteUseCase.execute(movieId).collect {
                val newState = state.value.copy(isFavourite = it != null)
                updateState(newState)
            }
        }

    }

    fun getMoviesData(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getMovieByIDUseCase.execute(movieId).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        val newState =
                            state.value.copy(movieByIdDataModel = it.data, isLoading = false)
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
                checkFavourite(movieId)
            }
        }
    }

    private fun deleteFavouriteMovieData(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFavouriteMovieUseCase.execute(movieId).collect {
                checkFavourite(movieId)
            }
        }
    }
}