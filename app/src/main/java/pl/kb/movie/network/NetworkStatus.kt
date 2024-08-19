package pl.kb.movie.network

sealed class NetworkStatus {
    data object Connected: NetworkStatus()
    data object Disconnected: NetworkStatus()
}