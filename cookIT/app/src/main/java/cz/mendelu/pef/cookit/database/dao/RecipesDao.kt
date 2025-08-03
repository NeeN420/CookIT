package cz.mendelu.pef.cookit.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.pef.cookit.database.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert
    suspend fun insert(task: Recipe): Long


    @Query("SELECT * FROM recipes")
    fun getAll(): Flow<List<Recipe>>

    @Update
    suspend fun update(task: Recipe)

    @Delete
    suspend fun delete(task: Recipe)

    @Query("UPDATE recipes SET taskState = :state WHERE id = :id")
    suspend fun changeTaskState(id: Long, state: Boolean)

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getById(id: Long): Recipe


}