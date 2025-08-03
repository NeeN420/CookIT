package cz.mendelu.pef.cookit.database.repository


import cz.mendelu.pef.cookit.database.dao.IngredientDao
import cz.mendelu.pef.cookit.database.model.Ingredient
import cz.mendelu.pef.cookit.database.model.Recipe
import cz.mendelu.pef.cookit.database.model.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class IngredientRepositoryImpl @Inject constructor(
    private val dao: IngredientDao
) : IIngredientRepository {
    override suspend fun insertIngredient(ingredient: Ingredient) {
        dao.insertIngredient(ingredient)
    }

    override  fun getIngredientsForTask(taskId: Long): Flow<List<Ingredient>> {
        return dao.getIngredientsForTask(taskId)
    }

    override suspend fun deleteIngredient(ingredient: Ingredient) {
        dao.deleteIngredient(ingredient)
    }

    override suspend fun deleteAllIngredientsForTask(taskId: Long) {
        dao.deleteAllIngredientsForTask(taskId)
    }

    override fun getTasksByIngredient(ingredient: String): Flow<List<Recipe>> {
        return dao.getTasksByIngredient(ingredient)
    }

    override fun getTasksWithIngredientsByIngredient(ingredient: String): Flow<List<RecipeWithIngredients>> {
        return dao.getTasksWithIngredientsByIngredient(ingredient)
    }


    override fun getAllTasksWithIngredients(): Flow<List<RecipeWithIngredients>> {
        return dao.getAllTasksWithIngredients()
    }
    override  fun getTaskWithIngredients(id: Long): Flow<RecipeWithIngredients?> {
        return dao.getTaskWithIngredients(id)
    }

    override suspend fun updateIngredientsForTask(taskId: Long, newIngredients: List<Ingredient>) {
        val existing = dao.getIngredientsForTask(taskId).first()

        val toUpdate = newIngredients.filter { it.id != null }
        val toInsert = newIngredients.filter { it.id == null }

        if (toUpdate.isNotEmpty()) {
            dao.updateIngredients(toUpdate) // Room @Update(onConflict...) s List
        }
        if (toInsert.isNotEmpty()) {
            dao.insertIngredients(toInsert.map { it.copy(taskId = taskId) })
        }

        val toDelete = existing.filter { e -> newIngredients.none { it.id == e.id } }
        toDelete.forEach { dao.deleteIngredient(it) }
    }

    override suspend fun updateIngredient(ingredient: Ingredient) {
        dao.updateIngredient(ingredient)
    }

    override suspend fun updateIngredients(ingredients: List<Ingredient>) {
        dao.updateIngredients(ingredients)
    }
}
