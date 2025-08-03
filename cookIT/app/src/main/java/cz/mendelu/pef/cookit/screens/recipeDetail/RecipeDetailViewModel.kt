package cz.mendelu.pef.cookit.screens.recipeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.cookit.database.repository.IIngredientRepository
import cz.mendelu.pef.cookit.database.repository.IShoppingListRepository
import cz.mendelu.pef.cookit.database.repository.IRecipesLocalRepository
import cz.mendelu.pef.cookit.database.model.ShoppingListItem
import cz.mendelu.pef.cookit.database.model.RecipeWithIngredients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: IRecipesLocalRepository,
    private val ingredientRepository: IIngredientRepository,
    private val Shoppingrepository: IShoppingListRepository
) : ViewModel() {

    private val _taskWithIngredients = MutableStateFlow<RecipeWithIngredients?>(null)
    val taskWithIngredients: StateFlow<RecipeWithIngredients?> = _taskWithIngredients

    fun loadRecipe(id: Long) {
        viewModelScope.launch {
            ingredientRepository.getTaskWithIngredients(id).collect { task ->
                _taskWithIngredients.value = task
            }
        }
    }

    fun addToShoppingList() {
        val ingredients = taskWithIngredients.value?.ingredients ?: return

        viewModelScope.launch {
            ingredients.forEach { ingredient ->
                val name = ingredient.name.trim().lowercase()
                val unit = ingredient.unit.trim().lowercase()
                val amount = ingredient.amount?.toDoubleOrNull() ?: return@forEach

                // Získání existující položky (už po ošetření velikosti a mezer)
                val existing = Shoppingrepository.getShoppingItemByNameAndUnit(name, unit)

                val totalAmount = (existing?.amount?.toDoubleOrNull() ?: 0.0) + amount

                // !!! Zde důležité: použít US locale pro správné formátování (2.50, ne 2,50)
                val formattedAmount = String.format(Locale.US, "%.2f", totalAmount)

                val item = ShoppingListItem(
                    id = existing?.id,
                    name = name,
                    amount = formattedAmount,
                    unit = unit,
                    checked = existing?.checked ?: false
                )

                if (existing != null) {
                    Shoppingrepository.updateShoppingItem(item)
                } else {
                    Shoppingrepository.insertShoppingItem(item)
                }
            }
        }
    }


}






