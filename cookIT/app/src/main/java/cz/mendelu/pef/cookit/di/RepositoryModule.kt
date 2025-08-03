package cz.mendelu.pef.cookit


import cz.mendelu.pef.cookit.database.repository.IIngredientRepository
import cz.mendelu.pef.cookit.database.repository.IShoppingListRepository
import cz.mendelu.pef.cookit.database.repository.IRecipesLocalRepository
import cz.mendelu.pef.cookit.database.dao.IngredientDao
import cz.mendelu.pef.cookit.database.dao.PlannedRecipeDao
import cz.mendelu.pef.cookit.database.repository.IngredientRepositoryImpl
import cz.mendelu.pef.cookit.database.dao.ShoppingListDao
import cz.mendelu.pef.cookit.database.repository.ShoppingListRepositoryImpl
import cz.mendelu.pef.cookit.database.dao.RecipesDao
import cz.mendelu.pef.cookit.database.repository.IPlannedRecipeRepository
import cz.mendelu.pef.cookit.database.repository.PlannedRecipeRepositoryImpl
import cz.mendelu.pef.cookit.database.repository.RecipesLocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(dao: RecipesDao): IRecipesLocalRepository {
        return RecipesLocalRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideIngredientRepository(dao: IngredientDao): IIngredientRepository {
        return IngredientRepositoryImpl(dao)
    }
    @Provides
    @Singleton
    fun provideRepository(dao: ShoppingListDao): IShoppingListRepository {
        return ShoppingListRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun providePlannedRecipeRepository(dao: PlannedRecipeDao): IPlannedRecipeRepository {
        return PlannedRecipeRepositoryImpl(dao)
    }
}