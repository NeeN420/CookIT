package cz.mendelu.pef.cookit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.pef.cookit.screens.addEditIngredients.AddEditIngredientScreen
import cz.mendelu.pef.cookit.screens.addEditRecipe.AddEditRecipeScreen
import cz.mendelu.pef.cookit.screens.Calendar.CalendarScreen
import cz.mendelu.pef.cookit.screens.recipeDetail.RecipeDetailScreen
import cz.mendelu.pef.cookit.screens.search.SearchScreen
import cz.mendelu.pef.cookit.screens.myRecipe.MyRecipeScreen
import cz.mendelu.pef.cookit.screens.shoppingList.ShoppingListScreen

@Composable
fun NavGraph(
    startDestination: String ,
    navHostController: NavHostController = rememberNavController(),
    navRouter: INavigationRouter = remember {
        NavigationRouterImpl(navHostController)
    }
) {
    NavHost(navController = navHostController, startDestination = startDestination) {

        composable(route = Destination.SearchScreen.route) {
            SearchScreen(navigation = navRouter) // předáš vlastní hiltViewModel uvnitř
        }

        composable(route = Destination.MyRecipeScreen.route) {
            MyRecipeScreen(navigation = navRouter)
        }

        composable(route = "${Destination.AddEditRecipeScreen.route}/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val id = it.arguments?.getLong("id")
            AddEditRecipeScreen(navRouter, if (id == -1L) null else id)
        }

        composable(route = Destination.AddEditRecipeScreen.route) {
            AddEditRecipeScreen(navRouter, null)
        }

        composable(
            route = "${Destination.RecipeDetailScreen.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            val id = it.arguments?.getLong("id") ?: return@composable
            RecipeDetailScreen(id = id, navigation = navRouter)
        }
        composable(route = Destination.ShoppingListScreen.route) {
            ShoppingListScreen(navigation = navRouter)
        }
        composable("addEditShoppingItem") {
            AddEditIngredientScreen(
                navigation = navRouter,
                id = null,

            )
        }

        composable("addEditShoppingItem/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
            AddEditIngredientScreen(navRouter, id)
        }
        composable(route = "calendar") {
            CalendarScreen(navigation = navRouter)
        }





    }
}