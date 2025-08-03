package cz.mendelu.pef.cookit.screens.Calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.pef.cookit.R
import cz.mendelu.pef.cookit.navigation.Destination
import cz.mendelu.pef.cookit.navigation.INavigationRouter
import cz.mendelu.pef.cookit.ui.theme.elements.BaseScreen
import cz.mendelu.pef.cookit.ui.theme.elements.BottomNavBar
import io.github.boguszpawlowski.composecalendar.Calendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState

@Composable
fun CalendarScreen(
    navigation: INavigationRouter,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val calendarState = rememberSelectableCalendarState()
    val selectedDate = calendarState.selectionState.selection.firstOrNull()
    val plannedRecipes by viewModel.plans.collectAsState()
    val context = LocalContext.current
    val appVersion = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "N/A"
    } catch (e: Exception) {
        "N/A"
    }

    // Při změně vybraného dne načti nové plánované recepty
    LaunchedEffect(selectedDate) {
        selectedDate?.let { viewModel.loadPlansForDate(it) }
    }

    BaseScreen(
        topBarText = stringResource(R.string.planned_recipes),
        showLoading = false,
        onBackClick = { navigation.returnBack() },
        bottomBar = {
            BottomNavBar(
                currentRoute = Destination.Calendar.route,
                navigation = navigation
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Kalendář
            Calendar(calendarState = calendarState)

            Spacer(modifier = Modifier.height(16.dp))

            // Vybraný den
            selectedDate?.let {
                Text(
                    text = stringResource(R.string.selected_day)+" ${it.dayOfMonth}.${it.monthValue}.${it.year}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Výpis plánovaných receptů
            if (plannedRecipes.isEmpty()) {
                Text(stringResource(R.string.no_recipes_for_this_day), color = Color.Gray)
            } else {
                LazyColumn {
                    items(plannedRecipes) { recipe ->
                        recipe.task.id?.let { recipeId ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = recipe.task.Title,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                navigation.navigateToRecipeDetail(recipeId)
                                            }
                                    )

                                    IconButton(onClick = {
                                        selectedDate?.let { date ->
                                            viewModel.deletePlannedRecipe(recipe, date)
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete recipe",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(R.string.app_version, appVersion),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
