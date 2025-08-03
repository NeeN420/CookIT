package cz.mendelu.pef.cookit.screens.addEditIngredients

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.cookit.R
import cz.mendelu.pef.cookit.database.repository.IShoppingListRepository
import cz.mendelu.pef.cookit.database.repository.IRecipesLocalRepository
import cz.mendelu.pef.cookit.database.model.ShoppingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditIngredientViewModel @Inject constructor(private val Reciperepository: IRecipesLocalRepository,
                                                     private val Shoppingrepository: IShoppingListRepository

) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditIngredientUIState())
    val uiState = _uiState.asStateFlow()

    fun loadIngredient(id: Long?) {
        if (id == null) return
        _uiState.value = _uiState.value.copy(loading = true)
        viewModelScope.launch {
            val item = Shoppingrepository.getShoppingItemById(id)
            _uiState.value = _uiState.value.copy(
                id = item?.id,
                name = item?.name.orEmpty(),
                amount = item?.amount.orEmpty(),
                unit = item?.unit.orEmpty(),
                loading = false
            )
        }
    }

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name, nameError = null)
    }

    fun onAmountChanged(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount, amountError = null)
    }

    fun onUnitChanged(unit: String) {
        _uiState.value = _uiState.value.copy(unit = unit)
    }

    fun saveIngredient() {
        val name = _uiState.value.name.trim()
        val amount = _uiState.value.amount.trim()

        if (name.isEmpty()) {
            _uiState.value = _uiState.value.copy(nameError = R.string.task_text_empty_error)
            return
        }

        if (amount.isEmpty()) {
            _uiState.value = _uiState.value.copy(amountError = R.string.ingredient_amount_error)
            return
        }

        val item = ShoppingListItem(
            id = _uiState.value.id,
            name = name,
            amount = amount,
            unit = _uiState.value.unit
        )

        Log.d("AddEditIngredient", "Saving item: $item") // ← musí být až po vytvoření

        viewModelScope.launch {
            if (item.id == null) {
                Shoppingrepository.insertShoppingItem(item)
            } else {
                Shoppingrepository.updateShoppingItem(item)
            }
            _uiState.value = _uiState.value.copy(saved = true)
        }
    }

}
