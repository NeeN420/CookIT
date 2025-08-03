package cz.mendelu.pef.cookit.database.dao


import androidx.room.*
import cz.mendelu.pef.cookit.database.model.Ingredient
import cz.mendelu.pef.cookit.database.model.Recipe
import cz.mendelu.pef.cookit.database.model.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Insert
    suspend fun insertIngredient(ingredient: Ingredient)

    @Insert
    suspend fun insertIngredients(ingredients: List<Ingredient>)

    @Query("SELECT * FROM ingredients WHERE taskId = :taskId")
    fun getIngredientsForTask(taskId: Long): Flow<List<Ingredient>>

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)

    @Query("DELETE FROM ingredients WHERE taskId = :taskId")
    suspend fun deleteAllIngredientsForTask(taskId: Long)

    @Transaction
    @Query("""
    SELECT * FROM recipes 
    WHERE id IN (
        SELECT taskId FROM ingredients 
        WHERE name LIKE '%' || :ingredient || '%'
    )
""")
    fun getTasksByIngredient(ingredient: String): Flow<List<Recipe>>


    @Transaction
    @Query("SELECT * FROM recipes WHERE id IN (SELECT taskId FROM ingredients WHERE name LIKE '%' || :ingredient || '%')")
    fun getTasksWithIngredientsByIngredient(ingredient: String): Flow<List<RecipeWithIngredients>>

    @Transaction
    @Query("SELECT * FROM recipes")
    fun getAllTasksWithIngredients(): Flow<List<RecipeWithIngredients>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getTaskWithIngredients(id: Long):  Flow<RecipeWithIngredients?>

    @Update
    suspend fun updateIngredient(ingredient: Ingredient)

    @Update
    suspend fun updateIngredients(ingredients: List<Ingredient>)
}
