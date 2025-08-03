package cz.mendelu.pef.cookit.database.repository
import cz.mendelu.pef.cookit.database.model.Recipe
import kotlinx.coroutines.flow.Flow

interface IRecipesLocalRepository {
    suspend fun insert(task: Recipe): Long
    fun getAll(): Flow<List<Recipe>>
    suspend fun update(task: Recipe)
    suspend fun delete(task: Recipe)
    suspend fun changeTaskState(id: Long, state: Boolean)
    suspend fun getById(id: Long): Recipe


}