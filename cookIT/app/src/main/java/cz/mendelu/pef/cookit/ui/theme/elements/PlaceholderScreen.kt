package cz.mendelu.pef.cookit.ui.theme.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.cookit.ui.theme.theme.basicMargin

data class PlaceholderScreenContent(
    val text: String? = null,
    val image: Int? = null
)

@Composable
fun PlaceholderScreen(
    paddingValues: PaddingValues,
    placeholderScreenContent: PlaceholderScreenContent
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = basicMargin,
                end = basicMargin,
                bottom = basicMargin
            )
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            placeholderScreenContent.image?.let { image ->
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp) // menší, úhlednější
                        .padding(bottom = 24.dp),
                    contentScale = ContentScale.Fit
                )
            }

            placeholderScreenContent.text?.let { text ->
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}










