package cz.mendelu.pef.cookit.screens.myRecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.cookit.database.repository.IIngredientRepository
import cz.mendelu.pef.cookit.database.model.RecipeWithIngredients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyRecipeViewModel @Inject constructor(
    private val repository: IIngredientRepository
) : ViewModel() {

    val recipes: StateFlow<List<RecipeWithIngredients>> =
        repository.getAllTasksWithIngredients()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())



}

