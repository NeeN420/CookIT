package cz.mendelu.pef.cookit.database.repository

import cz.mendelu.pef.cookit.database.model.ShoppingListItem
import kotlinx.coroutines.flow.Flow

interface IShoppingListRepository {
    suspend fun insertShoppingItem(item: ShoppingListItem)
    fun getAllShoppingItems(): Flow<List<ShoppingListItem>>
    suspend fun updateShoppingItem(item: ShoppingListItem)
    suspend fun deleteShoppingItem(item: ShoppingListItem)
    suspend fun clearShoppingList()
    suspend fun getShoppingItemById(id: Long): ShoppingListItem?
    suspend fun getShoppingItemByNameAndUnit(name: String, unit: String): ShoppingListItem?
    suspend fun getEquivalentShoppingItem(name: String, unit: String): ShoppingListItem?
    suspend fun insertOrUpdateShoppingItem(item: ShoppingListItem)

}
