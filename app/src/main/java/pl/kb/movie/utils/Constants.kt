package pl.kb.movie.utils

import pl.kb.movie.BuildConfig

class Constants {
    companion object {
        internal const val baseUrl: String = BuildConfig.baseUrl
        internal const val accessToken: String = BuildConfig.ACCESS_TOKEN
        internal const val DATABASE_NAME: String = BuildConfig.databaseName
        internal const val baseUrlThumbnail: String = BuildConfig.baseUrlThumbnail
    }
}