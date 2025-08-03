package cz.mendelu.pef.cookit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.mendelu.pef.cookit.database.dao.IngredientDao
import cz.mendelu.pef.cookit.database.dao.PlannedRecipeDao
import cz.mendelu.pef.cookit.database.dao.ShoppingListDao
import cz.mendelu.pef.cookit.database.model.Ingredient
import cz.mendelu.pef.cookit.database.model.ShoppingListItem
import cz.mendelu.pef.cookit.database.model.Recipe
import cz.mendelu.pef.cookit.database.dao.RecipesDao
import cz.mendelu.pef.cookit.database.model.PlannedRecipe

@Database(entities = [Recipe::class, Ingredient::class, ShoppingListItem::class, PlannedRecipe::class], version = 8, exportSchema = true)
@TypeConverters(Converters::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun tasksDao(): RecipesDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun shopingListDao(): ShoppingListDao
    abstract fun plannedRecipe(): PlannedRecipeDao

    companion object {
        private var INSTANCE: RecipesDatabase? = null
        fun getDatabase(context: Context): RecipesDatabase {
            if (INSTANCE == null) {
                synchronized(RecipesDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            RecipesDatabase::class.java,
                            "recipes_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}