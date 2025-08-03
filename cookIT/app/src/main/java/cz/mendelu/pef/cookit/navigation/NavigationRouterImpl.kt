package cz.mendelu.pef.cookit.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun navigateToSearch() {
        navController.navigate(Destination.SearchScreen.route)
    }

    override fun navigateToMyRecipes() {
        navController.navigate(Destination.MyRecipeScreen.route)
    }

    override fun navigateToAddRecipe() {
        navController.navigate(Destination.AddEditRecipeScreen.route)
    }

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun navigateToAddEditRecipe(id: Long?) {
        if (id != null) {
            navController.navigate("${Destination.AddEditRecipeScreen.route}/$id")
        } else {
            navController.navigate(Destination.AddEditRecipeScreen.route)
        }
    }
    override fun navigateToRecipeDetail(id: Long) {
        navController.navigate("${Destination.RecipeDetailScreen.route}/$id")
    }
    override fun navigateToShoppingList() {
        navController.navigate(Destination.ShoppingListScreen.route)
    }

    override fun navigateToAddEditIngridient(id: Long?) {

        val route = if (id != null) "addEditShoppingItem/$id" else "addEditShoppingItem"
        navController.navigate(route)
    }

    override fun navigateToCalendar() {
        navController.navigate("calendar")
    }



}
