package pl.kb.movie.presentation.ui.movies

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.compose.koinViewModel
import pl.kb.movie.R
import pl.kb.movie.domain.mappers.MovieDataModel
import pl.kb.movie.presentation.contracts.BaseContract
import pl.kb.movie.presentation.contracts.MoviesContractState
import pl.kb.movie.presentation.ui.components.EmptyView
import pl.kb.movie.presentation.ui.components.FavoriteButton
import pl.kb.movie.presentation.ui.components.LoadingBar
import pl.kb.movie.presentation.ui.theme.MovieTheme
import pl.kb.movie.utils.Constants
import pl.kb.movie.utils.TestTags

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = koinViewModel<MoviesViewModel>(),
    state: MoviesContractState = viewModel.state.collectAsState().value,
    effectFlow: Flow<BaseContract.Effect>? = viewModel.effects.receiveAsFlow(),
    onRefreshCall: () -> Unit = { viewModel.getMoviesData() },
    onItemClicked: (movieId: Int) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val movieMessage = stringResource(R.string.movies_are_loaded)

    LaunchedEffect(effectFlow) {
        viewModel.getFavouriteMovies()
        effectFlow?.onEach { effect ->
            if (effect is BaseContract.Effect.DataWasLoaded)
                snackBarHostState.showSnackbar(
                    message = movieMessage,
                    duration = SnackbarDuration.Short
                )
        }?.collect { value ->
            if (value is BaseContract.Effect.Error) {
                Toast.makeText(context, value.errorMessage ?: "", Toast.LENGTH_LONG).show()
            }
        }
    }
    Scaffold(
        topBar = {
            MovieAppBar(
                onRefreshCall = onRefreshCall,
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(
                bottom = paddingValues.calculateBottomPadding(),
                top = paddingValues.calculateTopPadding()
            )
        ) {
            ScreenContent(
                state,
                onItemClicked,
                viewModel::isFavouriteCheck,
                viewModel::favouriteClicked
            )
        }
    }
}

@Composable
fun ScreenContent(
    state: MoviesContractState,
    onItemClicked: (movieId: Int) -> Unit,
    isFavouriteCheck: (movieId: Int) -> Boolean,
    favouriteClicked: (isFavourite: Boolean, movieId: Int) -> Unit,
) {
    Surface(modifier = Modifier.semantics {
        testTag = TestTags.HOME_SCREEN_TAG
    }) {
        Box {
            val nowPlayingMovies = state.nowPlayingMovies
            if (state.isLoading) {
                LoadingBar()
            } else if (nowPlayingMovies == null) {
                EmptyView(message = stringResource(R.string.empty_list_text))
            } else {
                MoviesList(
                    movies = nowPlayingMovies.results,
                    isLoading = state.isLoading,
                    isFavouriteCheck = isFavouriteCheck,
                    favouriteClicked = favouriteClicked,
                ) { movieId ->
                    onItemClicked(movieId)
                }
            }
        }
    }
}

@Composable
fun MoviesList(
    isLoading: Boolean,
    movies: List<MovieDataModel>,
    isFavouriteCheck: (movieId: Int) -> Boolean,
    favouriteClicked: (isFavourite: Boolean, movieId: Int) -> Unit,
    onItemClicked: (movieId: Int) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        content = {
            this.items(movies) { movie ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    border = BorderStroke(0.5.dp, Color.Gray),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 5.dp, end = 5.dp, top = 10.dp)
                        .clickable {
                            if (!isLoading) {
                                onItemClicked(movie.id)
                            }
                        }
                        .semantics { testTag = TestTags.MOVIE_ITEM_TAG }
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth()
                            .padding(top = 1.dp),
                    ) {
                        ItemThumbnail(thumbnailUrl = movie.posterPath)
                        FavoriteButton(
                            isFavourite = isFavouriteCheck(movie.id),
                            favouriteClicked = {
                                favouriteClicked(it, movie.id)
                            },
                            modifier = Modifier.semantics { testTag = TestTags.TOGGLE_FAV_BUTTON })
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .align(Alignment.BottomStart),
                        ) {
                            Text(
                                text = movie.title,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                color = colorResource(id = R.color.black),
                            )
                        }
                    }
                }
            }
        }, modifier = Modifier
            .semantics { testTag = TestTags.MOVIES_LIST_TAG }
            .fillMaxSize()

    )
}

@Composable
fun ItemThumbnail(
    thumbnailUrl: String
) {
    GlideImage(
        imageModel = "${Constants.baseUrlThumbnail}$thumbnailUrl",
        modifier = Modifier
            .semantics { testTag = TestTags.LIST_IMG }
            .wrapContentSize()
            .wrapContentHeight()
            .defaultMinSize(
                minWidth = 500.dp,
                minHeight = 200.dp,
            )
            .fillMaxWidth(),
        contentScale = ContentScale.Crop,
        circularReveal = CircularReveal(duration = 200),
        shimmerParams = ShimmerParams(
            baseColor = Color.Red,
            highlightColor = Color.Red,
            durationMillis = 1000,
            dropOff = 0.55f,
            tilt = 20f,
        ),
        contentDescription = TestTags.MOVIE_THUMBNAIL_PICTURE,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieAppBar(
    onRefreshCall: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.semantics { testTag = TestTags.MOVIE_SCREEN_APP_BAR },
        title = {
            Text(
                text = stringResource(R.string.app_bar_title),
                color = colorResource(id = R.color.white),
            )
        },
        actions = {
            IconButton(
                onClick = onRefreshCall

            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = TestTags.REFRESH_ACTION
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color(R.color.white),
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MovieTheme {
        MoviesScreen(
            onItemClicked = {},
            onRefreshCall = {}
        )
    }
}
