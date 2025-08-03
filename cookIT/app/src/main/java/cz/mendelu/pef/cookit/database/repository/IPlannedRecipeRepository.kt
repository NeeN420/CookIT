package cz.mendelu.pef.cookit.database.repository

import cz.mendelu.pef.cookit.database.model.PlannedRecipe
import cz.mendelu.pef.cookit.database.model.PlannedRecipeWithTask
import java.time.LocalDate

interface IPlannedRecipeRepository {
    suspend fun insert(plan: PlannedRecipe)
    suspend fun getPlansForDate(date: LocalDate): List<PlannedRecipeWithTask>
    suspend fun delete(plan: PlannedRecipe)

}
