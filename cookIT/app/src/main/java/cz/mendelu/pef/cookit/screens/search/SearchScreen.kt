package cz.mendelu.pef.cookit.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.pef.cookit.R
import cz.mendelu.pef.cookit.ui.theme.elements.BaseScreen
import cz.mendelu.pef.cookit.database.model.RecipeWithIngredients
import cz.mendelu.pef.cookit.navigation.Destination
import cz.mendelu.pef.cookit.navigation.INavigationRouter
import cz.mendelu.pef.cookit.ui.theme.elements.BottomNavBar
import cz.mendelu.pef.cookit.ui.theme.elements.PlaceholderScreen
import cz.mendelu.pef.cookit.ui.theme.elements.PlaceholderScreenContent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigation: INavigationRouter
) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val query by viewModel.searchQuery.collectAsState()
    val tasks by viewModel.matchedRecipes.collectAsStateWithLifecycle()
    val allrecipes by viewModel.allrecipes.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsState()

    BaseScreen(
        topBarText = stringResource(R.string.app_name),
        showLoading = false,
        onBackClick = { },
        bottomBar = {
            BottomNavBar(
                currentRoute = Destination.SearchScreen.route,
                navigation = navigation
            )
        },
        placeholderScreenContent = if (allrecipes.size == 0) {
            PlaceholderScreenContent(
                text = stringResource(R.string.you_have_no_recipes_to_search_from),
                image = R.drawable.undraw_accept_request_489a
            )
        } else null

    ) {
        SearchScreenContent(
            paddingValues = it,
            query = query,
            tasks = tasks,
            onQueryChange = viewModel::onSearchQueryChanged,
            navigation = navigation,
            viewModel = viewModel
        )
    }
}

@Composable
fun SearchScreenContent(
    paddingValues: PaddingValues,
    query: String,
    tasks: List<RecipeWithIngredients>,
    onQueryChange: (String) -> Unit,
    navigation: INavigationRouter,
    viewModel: SearchViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(stringResource(R.string.enter_the_ingredients)) },
            leadingIcon = { Icon(Icons.Default.Menu, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                    shape = RoundedCornerShape(12.dp)
        )
        when {


            tasks.isEmpty() -> {
                // 2️⃣ Žádný výsledek odpovídající filtru
                PlaceholderScreen(
                    paddingValues = PaddingValues(0.dp),
                    placeholderScreenContent = PlaceholderScreenContent(
                        image = R.drawable.undraw_accept_request_489a,
                        text = stringResource(R.string.no_recipe_matches)
                    )
                )
            }

            else -> {

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(tasks) { taskWithIngredients ->
                        val ingredientText =
                            taskWithIngredients.ingredients.joinToString { it.name }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    navigation.navigateToRecipeDetail(
                                        taskWithIngredients.task.id ?: 0L
                                    )
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = taskWithIngredients.task.Title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = stringResource(R.string.ingredients)+": $ingredientText",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
