package cz.mendelu.pef.cookit.database.repository

import cz.mendelu.pef.cookit.database.dao.ShoppingListDao
import cz.mendelu.pef.cookit.database.model.ShoppingListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val dao: ShoppingListDao
) : IShoppingListRepository {

    override suspend fun insertShoppingItem(item: ShoppingListItem) {
        dao.insertShoppingItem(item)
    }

    override fun getAllShoppingItems(): Flow<List<ShoppingListItem>> {
        return dao.getAllShoppingItems()
    }

    override suspend fun updateShoppingItem(item: ShoppingListItem) {
        dao.updateShoppingItem(item)
    }

    override suspend fun deleteShoppingItem(item: ShoppingListItem) {
        dao.deleteShoppingItem(item)
    }

    override suspend fun clearShoppingList() {
        dao.clearShoppingList()
    }
    override suspend fun getShoppingItemById(id: Long): ShoppingListItem? {
        return dao.getShoppingItemById(id)
    }

    override suspend fun getShoppingItemByNameAndUnit(name: String, unit: String): ShoppingListItem?{
        return dao.getShoppingItemByNameAndUnit(name, unit)
    }
    override suspend fun getEquivalentShoppingItem(name: String, unit: String): ShoppingListItem? {
        val allItems = dao.getAllShoppingItems().first() // přidáme si variantu bez Flow
        val baseUnit = if (unit == "kg") "g" else if (unit == "l") "ml" else unit

        return allItems.find { existing ->
            existing.name.trim() == name.trim() &&
                    (existing.unit == unit || existing.unit == baseUnit)
        }

    }

    override suspend fun insertOrUpdateShoppingItem(item: ShoppingListItem) {
        val existing = item.unit?.let { dao.getShoppingItemByNameAndUnit(item.name, it) }
        if (existing != null) {
            val existingAmount = existing.amount?.toDoubleOrNull() ?: 0.0
            val newAmount = item.amount?.toDoubleOrNull() ?: 0.0
            val total = String.format("%.2f", existingAmount + newAmount)

            val updatedItem = item.copy(
                id = existing.id,
                amount = total,
                checked = existing.checked
            )
            dao.updateShoppingItem(updatedItem)
        } else {
            dao.insertShoppingItem(item)
        }
    }

}
