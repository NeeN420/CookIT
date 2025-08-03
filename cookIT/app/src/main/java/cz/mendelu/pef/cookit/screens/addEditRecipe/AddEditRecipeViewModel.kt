package cz.mendelu.pef.cookit.screens.addEditRecipe


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.cookit.R
import cz.mendelu.pef.cookit.database.repository.IIngredientRepository
import cz.mendelu.pef.cookit.database.repository.IRecipesLocalRepository
import cz.mendelu.pef.cookit.database.model.Ingredient
import cz.mendelu.pef.cookit.database.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditRecipeViewModel @Inject constructor(
    private val repository: IRecipesLocalRepository,
    private val ingredientRepository: IIngredientRepository
) : ViewModel(), AddEditRecipeActions {

    private val _uiState : MutableStateFlow<AddEditRecipeUIState> =
        MutableStateFlow(value = AddEditRecipeUIState())
    val uiState = _uiState.asStateFlow()





    override fun onCurrentIngredientChanged(ingredient: String) {
        _uiState.update {
            it.copy(currentIngredient = ingredient)
        }
    }

    override fun onCurrentAmountChanged(amount: String) {
        _uiState.update { it.copy(currentAmount = amount) }
    }

    override fun addIngredient() {
        val name = _uiState.value.currentIngredient.trim()
        val amount = _uiState.value.currentAmount.trim()

        if (amount.isBlank()) {
            _uiState.update { it.copy(ingredientAmountError = R.string.task_text_empty_error) }
            return
        }

        if (name.isNotEmpty() && amount.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    ingredients = it.ingredients + Ingredient(
                        name = name,
                        amount = "$amount ",
                        unit = "${_uiState.value.currentUnit}",
                        taskId = it.task.id ?: 0
                    ),
                    currentIngredient = "",
                    currentAmount = "",
                    ingredientAmountError = null // reset error
                )
            }
        }
    }



    fun loadRecipe(id: Long?) {

            if (id != null) {
                viewModelScope.launch {
                    ingredientRepository.getTaskWithIngredients(id).collect { taskWithIngredients ->
                    if (taskWithIngredients != null) {
                        _uiState.update {
                            it.copy(
                                task = taskWithIngredients.task,
                                ingredients = taskWithIngredients.ingredients,
                                loading = false
                            )
                        }
                    } else {
                        // recept byl mezitím smazán, loading musí skončit!
                        _uiState.update {
                            it.copy(
                                task = Recipe(Title = "", Process = ""),
                                ingredients = emptyList(),
                                loading = false,
                                recipeDeleted = true // pojistka
                            )
                        }
                    }
                }
            }
        }
            else {

                _uiState.value = _uiState.value.copy(
                    loading = false,
                    task = Recipe(Title = "", Process = "")                )
            }
    }









    override fun saveRecipe() {
        viewModelScope.launch {
            val task = uiState.value.task

            if (task.Title.isBlank()) {
                _uiState.update { it.copy(taskTextError = R.string.task_text_empty_error) }
                return@launch
            }

            val taskId = if (task.id == null) {
                repository.insert(task)
            } else {
                repository.update(task)
                task.id
            } ?: return@launch

            ingredientRepository.updateIngredientsForTask(taskId, uiState.value.ingredients)

            _uiState.update { it.copy(recipeSaved = true) }
        }
    }


    override fun deleteRecipe() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(loading = true)
            }
            repository.delete(uiState.value.task)
            _uiState.update {
                it.copy(
                    recipeDeleted = true,
                    loading = false // <-- ujisti se, že toto tam je
                )
            }
        }
    }






    override fun onTitleChanged(title: String) {
        _uiState.update {
            it.copy(task = it.task.copy(Title = title))
        }
    }

    override fun onProcessChanged(process: String) {
        _uiState.update {
            it.copy(task = it.task.copy(Process = process))
        }
    }

    override fun removeIngredient(index: Int) {
        _uiState.update {
            it.copy(
                ingredients = it.ingredients.toMutableList().apply { removeAt(index) }
            )
        }
    }

   override fun onImageCaptured(uri: Uri) {
        _uiState.update {
            it.copy(task = it.task.copy(imageUri = uri.toString()))
        }
    }

    override fun onCurrentUnitChanged(unit: String) {
        _uiState.update {
            it.copy(currentUnit = unit)
        }
    }



}
