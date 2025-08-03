package cz.mendelu.pef.cookit.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.pef.cookit.database.model.ShoppingListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(item: ShoppingListItem)


    @Query("SELECT * FROM shopping_list_items")
    fun getAllShoppingItems(): Flow<List<ShoppingListItem>>

    @Update
    suspend fun updateShoppingItem(item: ShoppingListItem)

    @Delete
    suspend fun deleteShoppingItem(item: ShoppingListItem)

    @Query("DELETE FROM shopping_list_items")
    suspend fun clearShoppingList()

    @Query("SELECT * FROM shopping_list_items WHERE id = :id")
    suspend fun getShoppingItemById(id: Long): ShoppingListItem?

    @Query("SELECT * FROM shopping_list_items WHERE LOWER(TRIM(name)) = LOWER(TRIM(:name)) AND LOWER(TRIM(unit)) = LOWER(TRIM(:unit)) LIMIT 1")
    suspend fun getShoppingItemByNameAndUnit(name: String, unit: String): ShoppingListItem?

}
