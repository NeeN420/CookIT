package cz.mendelu.pef.cookit.database.repository


import cz.mendelu.pef.cookit.database.model.Ingredient
import cz.mendelu.pef.cookit.database.model.Recipe
import cz.mendelu.pef.cookit.database.model.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow

interface IIngredientRepository {
    suspend fun insertIngredient(ingredient: Ingredient)
    fun getIngredientsForTask(taskId: Long): Flow<List<Ingredient>>
    suspend fun deleteIngredient(ingredient: Ingredient)
    suspend fun deleteAllIngredientsForTask(taskId: Long)
    fun getTasksByIngredient(ingredient: String): Flow<List<Recipe>>
    fun getTasksWithIngredientsByIngredient(ingredient: String): Flow<List<RecipeWithIngredients>>
    fun getAllTasksWithIngredients(): Flow<List<RecipeWithIngredients>>
    fun getTaskWithIngredients(id: Long): Flow<RecipeWithIngredients?>
    suspend fun updateIngredientsForTask(taskId: Long, newIngredients: List<Ingredient>)
    suspend fun updateIngredient(ingredient: Ingredient)
    suspend fun updateIngredients(ingredients: List<Ingredient>)
}
