package cz.mendelu.pef.cookit.database.repository

import cz.mendelu.pef.cookit.database.dao.PlannedRecipeDao
import cz.mendelu.pef.cookit.database.model.PlannedRecipe
import cz.mendelu.pef.cookit.database.model.PlannedRecipeWithTask
import java.time.LocalDate
import javax.inject.Inject

class PlannedRecipeRepositoryImpl @Inject constructor(
    private val dao: PlannedRecipeDao
): IPlannedRecipeRepository {

    override suspend fun insert(plan: PlannedRecipe) = dao.insert(plan)

    override suspend fun getPlansForDate(date: LocalDate): List<PlannedRecipeWithTask> {
        return dao.getPlansForDate(date)
    }
    override suspend fun delete(plan: PlannedRecipe) = dao.delete(plan)


}
