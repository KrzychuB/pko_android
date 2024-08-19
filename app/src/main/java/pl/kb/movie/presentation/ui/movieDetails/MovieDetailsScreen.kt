package pl.kb.movie.presentation.ui.movieDetails

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import org.koin.androidx.compose.koinViewModel
import pl.kb.movie.R
import pl.kb.movie.domain.mappers.MovieByIdDataModel
import pl.kb.movie.presentation.contracts.MovieContractState
import pl.kb.movie.presentation.ui.components.EmptyView
import pl.kb.movie.presentation.ui.components.FavoriteButton
import pl.kb.movie.presentation.ui.components.LoadingBar
import pl.kb.movie.utils.Constants
import pl.kb.movie.utils.TestTags

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MoviesDetails(
    viewModel: MovieDetailsViewModel = koinViewModel<MovieDetailsViewModel>(),
    state: MovieContractState = viewModel.state.collectAsState().value,
    movieId: Int,
    onBackPressed: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getMoviesData(movieId)
        viewModel.checkFavourite(movieId)
    }

    Scaffold(
        topBar = {
            MovieDetailsAppBar(
                isFavourite = state.isFavourite,
                onBackPressed = onBackPressed,
                favouriteClicked = {
                    viewModel.favouriteClicked(it, movieId)
                }
            )
        }
    ) {
        Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            ScreenContent(state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailsAppBar(
    isFavourite: Boolean,
    favouriteClicked: (isFavourite: Boolean) -> Unit,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.semantics { testTag = TestTags.MOVIE_SCREEN_APP_BAR },
        title = {
            Text(
                text = stringResource(R.string.app_bar_title_details),
                color = colorResource(id = R.color.white),
            )
        },
        actions = {
            FavoriteButton(
                isFavourite = isFavourite,
                favouriteClicked = favouriteClicked,
                modifier = Modifier.semantics { testTag = TestTags.TOGGLE_FAV_BUTTON })
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color(R.color.white),
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = TestTags.BACK_NAVIGATION_ICON_DESCRIPTION,
                )
            }
        }
    )
}

@Composable
fun ScreenContent(
    state: MovieContractState,
) {
    Surface(modifier = Modifier.semantics {
        testTag = TestTags.HOME_SCREEN_TAG
    }) {
        Box {
            val movieByIdDataModel = state.movieByIdDataModel
            if (state.isLoading) {
                LoadingBar()
            } else if (movieByIdDataModel == null) {
                EmptyView(message = stringResource(R.string.empty_details_text))
            } else {
                MovieDetails(movieByIdDataModel = movieByIdDataModel)
            }
        }
    }
}

@Composable
fun MovieDetails(
    movieByIdDataModel: MovieByIdDataModel,
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Image(movieByIdDataModel.posterPath)
        MovieDescription(R.string.title_header, movieByIdDataModel.title)
        MovieDescription(R.string.original_title_header, movieByIdDataModel.originalTitle)
        MovieDescription(R.string.release_date_title_header, movieByIdDataModel.releaseDate)
        MovieDescription(R.string.rate_title_header, movieByIdDataModel.voteAverage.toString())
        MovieDescription(R.string.description_title_header, movieByIdDataModel.overview)
    }
}

@Composable
fun MovieDescription(@StringRes header: Int, description: String?) {
    description?.run {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(header),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(text = description)
        }
    }
}

@Composable
fun Image(thumbnailUrl: String?) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    val maxZoom = 4f
    thumbnailUrl?.let {
        GlideImage(
            imageModel = "${Constants.baseUrlThumbnail}$thumbnailUrl",
            modifier = Modifier
                .semantics { testTag = TestTags.FULL_IMG_DESCRIPTION }
                .wrapContentHeight()
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale *= zoom
                        scale = scale.coerceIn(1f, maxZoom)

                        offset = if (scale == 1f) {
                            Offset(0f, 0f)
                        } else {
                            val newOffsetX = offset.x + pan.x * scale
                            val newOffsetY = offset.y + pan.y * scale

                            Offset(newOffsetX, newOffsetY)
                        }
                    }
                }
                .graphicsLayer(
                    scaleX = maxOf(1f, minOf(maxZoom, scale)),
                    scaleY = maxOf(1f, minOf(maxZoom, scale)),
                    translationX = maxOf(-maxZoom, minOf(maxZoom, offset.x)),
                    translationY = maxOf(-maxZoom, minOf(maxZoom, offset.y))
                )
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollState()
                ),
            circularReveal = CircularReveal(duration = 100),
            shimmerParams = ShimmerParams(
                baseColor = MaterialTheme.colorScheme.background,
                highlightColor = Color.Gray,
                durationMillis = 500,
                dropOff = 0.55f,
                tilt = 20f
            ),
            contentDescription = TestTags.FULL_IMG_DESCRIPTION
        )
    }
}