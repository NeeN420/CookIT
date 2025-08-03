package cz.mendelu.pef.cookit.database.repository

import cz.mendelu.pef.cookit.database.model.Recipe
import cz.mendelu.pef.cookit.database.dao.RecipesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipesLocalRepositoryImpl @Inject constructor(private val tasksDao: RecipesDao) :
    IRecipesLocalRepository {



    override suspend fun insert(task: Recipe): Long {
        return tasksDao.insert(task)
    }

    override fun getAll(): Flow<List<Recipe>> {
        return tasksDao.getAll()
    }

    override suspend fun update(task: Recipe) {
        tasksDao.update(task)
    }

    override suspend fun delete(task: Recipe) {
        tasksDao.delete(task)
    }

    override suspend fun changeTaskState(id: Long, state: Boolean) {
        tasksDao.changeTaskState(id, state)
    }

    override suspend fun getById(id: Long): Recipe {
        return tasksDao.getById(id)
    }






}