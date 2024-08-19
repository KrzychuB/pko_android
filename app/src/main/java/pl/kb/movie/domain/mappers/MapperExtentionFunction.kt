package pl.kb.movie.domain.mappers

import pl.kb.movie.data.models.BelongsToCollectionResponse
import pl.kb.movie.data.models.DatesResponse
import pl.kb.movie.data.models.GenresResponse
import pl.kb.movie.data.models.MovieByIdResponse
import pl.kb.movie.data.models.MovieResponse
import pl.kb.movie.data.models.NowPlayingMoviesResponse
import pl.kb.movie.data.models.ProductionCompaniesResponse
import pl.kb.movie.data.models.ProductionCountriesResponse
import pl.kb.movie.data.models.SpokenLanguagesResponse

fun NowPlayingMoviesResponse.mapNowPlayingMoviesDataItems(): NowPlayingMoviesDataModel {
    return NowPlayingMoviesDataModel(
        dates = this.dates.mapDatesDataItems(),
        page = this.page,
        results = this.results.map { movie -> movie.mapMovieDataItems() },
        totalPages = this.totalPages,
        totalResults = this.totalResults,
    )
}

fun DatesResponse.mapDatesDataItems(): DatesDataModel {
    return DatesDataModel(
        maximum = this.maximum,
        minimum = this.minimum
    )
}

fun MovieResponse.mapMovieDataItems(): MovieDataModel {
    return MovieDataModel(
        id = this.id,
        adult = this.adult,
        backdropPath = this.backdropPath,
        genreIds = this.genreIds,
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
    )
}

fun MovieByIdResponse.mapMovieByIdDataItems(): MovieByIdDataModel {
    return MovieByIdDataModel(
        adult = this.adult,
        backdropPath = this.backdropPath,
        belongsToCollection = this.belongsToCollection?.mapBelongsToCollectionDataItems(),
        budget = this.budget,
        genres = this.genres.map { g -> g.mapGenresDataItem() },
        homepage = this.homepage,
        id = this.id,
        imdbId = this.imdbId,
        originCountry = this.originCountry,
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.posterPath,
        productionCompanies = this.productionCompanies.map { p -> p.mapProductionCompaniesDataItem() },
        productionCountries = this.productionCountries.map { p -> p.mapProductionCountriesDataItem() },
        releaseDate = this.releaseDate,
        revenue = this.revenue,
        runtime = this.runtime,
        spokenLanguages = this.spokenLanguages.map { s -> s.mapSpokenLanguagesDataItem() },
        status = this.status,
        tagline = this.tagline,
        title = this.title,
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,

        )
}

fun BelongsToCollectionResponse.mapBelongsToCollectionDataItems(): BelongsToCollectionDataModel {
    return BelongsToCollectionDataModel(
        id = this.id,
        name = this.name,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
    )
}

fun GenresResponse.mapGenresDataItem(): GenresDataModel {
    return GenresDataModel(
        id = this.id,
        name = this.name,
    )
}

fun ProductionCompaniesResponse.mapProductionCompaniesDataItem(): ProductionCompaniesDataModel {
    return ProductionCompaniesDataModel(
        id = this.id,
        logoPath = this.logoPath,
        name = this.name,
        originCountry = this.originCountry,
    )
}

fun ProductionCountriesResponse.mapProductionCountriesDataItem(): ProductionCountriesDataModel {
    return ProductionCountriesDataModel(
        iso31661 = this.iso31661,
        name = this.name,
    )
}

fun SpokenLanguagesResponse.mapSpokenLanguagesDataItem(): SpokenLanguagesDataModel {
    return SpokenLanguagesDataModel(
        englishName = this.englishName,
        iso6391 = this.iso6391,
        name = this.name,
    )
}