package cz.mendelu.pef.cookit.ui.theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val primary = Color(0xFF16268B)
val secondary = Color(0xFF2D42D0)
val tertiary = Color(0xFF46E54D)

val lightTextColor = Color(0xFF16268B)
val darkTextColor = Color(0xFF16268B)

@Composable
fun textColor() = if (isSystemInDarkTheme()) darkTextColor else lightTextColor

