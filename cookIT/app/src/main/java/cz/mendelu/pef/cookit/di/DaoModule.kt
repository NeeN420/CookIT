package cz.mendelu.pef.cookit




import cz.mendelu.pef.cookit.database.dao.IngredientDao
import cz.mendelu.pef.cookit.database.dao.ShoppingListDao
import cz.mendelu.pef.cookit.database.dao.RecipesDao
import cz.mendelu.pef.cookit.database.RecipesDatabase
import cz.mendelu.pef.cookit.database.dao.PlannedRecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideTaskDao(database: RecipesDatabase): RecipesDao {
        return database.tasksDao()
    }
    @Provides
    @Singleton
    fun provideIngredientDao(database: RecipesDatabase): IngredientDao {
        return database.ingredientDao()
    }
    @Provides
    @Singleton
    fun provideShoppingDao(database: RecipesDatabase): ShoppingListDao {
        return database.shopingListDao()
    }

    @Provides
    @Singleton
    fun providePlannedRecipeDao(database: RecipesDatabase): PlannedRecipeDao {
        return database.plannedRecipe()
    }
}