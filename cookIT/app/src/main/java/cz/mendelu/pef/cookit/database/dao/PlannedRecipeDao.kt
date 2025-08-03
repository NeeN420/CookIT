package cz.mendelu.pef.cookit.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import cz.mendelu.pef.cookit.database.model.PlannedRecipe
import cz.mendelu.pef.cookit.database.model.PlannedRecipeWithTask
import java.time.LocalDate

@Dao
interface PlannedRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plan: PlannedRecipe)

    @Query("SELECT * FROM planned_recipes WHERE date = :date")
    suspend fun getPlansForDate(date: LocalDate): List<PlannedRecipeWithTask>

    @Delete
    suspend fun delete(plan: PlannedRecipe)



}

