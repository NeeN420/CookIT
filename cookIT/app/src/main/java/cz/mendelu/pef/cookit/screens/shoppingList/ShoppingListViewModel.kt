package cz.mendelu.pef.cookit.screens.shoppingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.cookit.database.repository.IShoppingListRepository
import cz.mendelu.pef.cookit.database.model.ShoppingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val Shoppingrepository: IShoppingListRepository
) : ViewModel() {

    val items: StateFlow<List<ShoppingListItem>> = Shoppingrepository.getAllShoppingItems()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun addItem(name: String, amount: String?, unit: String?) {
        viewModelScope.launch {
            Shoppingrepository.insertShoppingItem(ShoppingListItem(name = name, amount = amount, unit = unit))
        }
    }

    fun toggleCheck(item: ShoppingListItem) {
        viewModelScope.launch {
            Shoppingrepository.updateShoppingItem(item.copy(checked = !item.checked))
        }
    }

    fun clearList() {
        viewModelScope.launch { Shoppingrepository.clearShoppingList() }
    }
    fun removeCheckedItems() {
        viewModelScope.launch {
            val currentItems = items.value
            currentItems.filter { it.checked }.forEach {
                Shoppingrepository.deleteShoppingItem(it)
            }
           // loadItems() // nebo emituj aktualizovaný seznam
        }
    }

}
