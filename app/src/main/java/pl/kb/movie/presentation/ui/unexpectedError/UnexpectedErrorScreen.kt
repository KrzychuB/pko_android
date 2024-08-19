package pl.kb.movie.presentation.ui.unexpectedError

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pl.kb.movie.R
import pl.kb.movie.presentation.ui.components.EmptyView
import pl.kb.movie.presentation.ui.theme.MovieTheme
import pl.kb.movie.utils.TestTags

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UnexpectedErrorScreen(
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            MovieDetailsAppBar(
                onBackPressed = onBackPressed,
            )
        }
    ) {
        Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            EmptyView(message = stringResource(R.string.empty_details_text))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailsAppBar(
    onBackPressed: () -> Unit
) {
    TopAppBar(
        modifier = Modifier,
        title = {
            Text(
                text = stringResource(R.string.app_bar_title_unexpected_error),
                color = colorResource(id = R.color.white),
            )
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

@Preview(showBackground = true)
@Composable
fun UnexpectedErrorScreenPreview() {
    UnexpectedErrorScreen(
        onBackPressed = {}
    )
}