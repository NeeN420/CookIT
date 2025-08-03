package cz.mendelu.pef.cookit.screens.myRecipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.cookit.R
import cz.mendelu.pef.cookit.navigation.Destination
import cz.mendelu.pef.cookit.navigation.INavigationRouter
import cz.mendelu.pef.cookit.ui.theme.elements.BaseScreen
import cz.mendelu.pef.cookit.database.model.RecipeWithIngredients
import cz.mendelu.pef.cookit.ui.theme.elements.BottomNavBar
import cz.mendelu.pef.cookit.ui.theme.elements.PlaceholderScreenContent


@Composable
fun MyRecipeScreen(
    navigation: INavigationRouter
) {
    val viewModel = hiltViewModel<MyRecipeViewModel>()
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()


    BaseScreen(
        topBarText = stringResource(R.string.my_recipe),
        showLoading = false,
        onBackClick = { navigation.returnBack() },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigation.navigateToAddEditRecipe(null)
            }) {
                Icon(Icons.Outlined.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Destination.MyRecipeScreen.route,
                navigation = navigation
            )
        },
                placeholderScreenContent = if (recipes.size == 0) {
            PlaceholderScreenContent(
                text = stringResource(R.string.add_your_first_recipe),
                image = R.drawable.undraw_accept_request_489a
            )
        } else null
    ) {
        MyRecipesScreenContent(
            paddingValues = it,
            recipes = recipes,
            navigation = navigation
        )
    }
}

@Composable
fun MyRecipesScreenContent(
    paddingValues: PaddingValues,
    recipes: List<RecipeWithIngredients>,
    navigation: INavigationRouter
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        items(recipes) { taskWithIngredients ->
            val ingredientText = taskWithIngredients.ingredients.joinToString { it.name }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        navigation.navigateToRecipeDetail(taskWithIngredients.task.id ?: 0L)
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
