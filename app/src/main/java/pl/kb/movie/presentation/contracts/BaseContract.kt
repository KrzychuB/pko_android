package pl.kb.movie.presentation.contracts

open class BaseContract {
    sealed class Effect {
        data object DataWasLoaded : Effect()
        data class Error(val errorMessage: String?) : Effect()
    }
}