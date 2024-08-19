package pl.kb.movie.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import pl.kb.movie.R

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    isFavourite: Boolean,
    favouriteClicked: (isFavourite: Boolean) -> Unit = {}
) {
    IconToggleButton(
        checked = isFavourite,
        onCheckedChange = {
            favouriteClicked(it)
        }
    ) {

        Icon(
            tint = color,
            modifier = modifier.graphicsLayer {
                scaleX = 1.5f
                scaleY = 1.5f
            },
            imageVector = if (isFavourite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = stringResource(R.string.favourite_icon)
        )
    }
}