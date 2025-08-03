package cz.mendelu.pef.cookit.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.cookit.database.repository.IIngredientRepository
import cz.mendelu.pef.cookit.database.repository.IRecipesLocalRepository
import cz.mendelu.pef.cookit.database.model.Ingredient
import cz.mendelu.pef.cookit.database.model.RecipeWithIngredients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: IRecipesLocalRepository,
    private val ingredientRepository: IIngredientRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            ingredientRepository.getAllTasksWithIngredients().collect {
                _allTasksWithIngredients.value = it
                _isLoading.value = false // <- zde se ukončí loading
            }
        }
    }


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _allTasksWithIngredients = MutableStateFlow<List<RecipeWithIngredients>>(emptyList())

    val allrecipes: StateFlow<List<RecipeWithIngredients>> =
        ingredientRepository.getAllTasksWithIngredients()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    // výsledky po filtrování více ingrediencí
    val matchedRecipes: StateFlow<List<RecipeWithIngredients>> = combine(
        _searchQuery,
        _allTasksWithIngredients
    ) { query, allTasks ->
        val terms = query.split(",")
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() }

        if (terms.isEmpty()) {
            allTasks
        } else {
            allTasks.filter { task ->
                val ingredientNames = task.ingredients.map { it.name.lowercase() }
                terms.all { term -> ingredientNames.any { it.contains(term) } }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            ingredientRepository.getAllTasksWithIngredients().collect {
                _allTasksWithIngredients.value = it
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun getIngredientsForTask(taskId: Long): Flow<List<Ingredient>> {
        return ingredientRepository.getIngredientsForTask(taskId)
    }
}