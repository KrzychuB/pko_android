package pl.kb.movie.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import pl.kb.movie.presentation.ui.theme.Pink40
import pl.kb.movie.utils.TestTags

@Composable
fun LoadingBar() {
    Box(
        modifier = Modifier
            .semantics { testTag = TestTags.LOADING_BAR_TAG }
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center

    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .testTag(TestTags.PROGRESS_BAR)
                .size(60.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 5.dp,
            trackColor = Pink40,
            strokeCap = StrokeCap.Round
        )
    }
}