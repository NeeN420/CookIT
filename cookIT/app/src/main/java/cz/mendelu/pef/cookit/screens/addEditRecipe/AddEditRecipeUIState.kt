package cz.mendelu.pef.cookit.screens.addEditRecipe

import cz.mendelu.pef.cookit.database.model.Ingredient
import cz.mendelu.pef.cookit.database.model.Recipe

data class AddEditRecipeUIState(
    val task: Recipe = Recipe(
        "",
        Process = "",

    ),
    val process: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val currentIngredient: String = "",
    val currentAmount: String = "",
    val currentUnit: String = "g",
    val titleError: Int? = null,
    val loading: Boolean = true,
    val recipeSaved: Boolean = false,
    val recipeDeleted: Boolean = false,
    val taskTextError: Int? = null,
    val ingredientAmountError: Int? = null

)