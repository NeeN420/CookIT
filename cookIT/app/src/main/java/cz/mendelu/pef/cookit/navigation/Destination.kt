package cz.mendelu.pef.cookit.navigation

sealed class Destination(val route: String) {
    object SearchScreen : Destination("search")
    object MyRecipeScreen : Destination("my_recipes")
    object AddEditRecipeScreen : Destination("add_edit_recipe")
    object RecipeDetailScreen : Destination("recipe_detail")
    object ShoppingListScreen : Destination("shoppingList")
    object Calendar : Destination("calendar")

}