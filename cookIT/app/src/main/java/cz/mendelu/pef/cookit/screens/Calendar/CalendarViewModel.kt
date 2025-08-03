package cz.mendelu.pef.cookit.screens.Calendar

import cz.mendelu.pef.cookit.database.repository.IPlannedRecipeRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.cookit.database.model.PlannedRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.cookit.database.model.PlannedRecipeWithTask
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repo: IPlannedRecipeRepository
) : ViewModel() {

    private val _plans = MutableStateFlow<List<PlannedRecipeWithTask>>(emptyList())
    val plans: StateFlow<List<PlannedRecipeWithTask>> = _plans

    var selectedRecipeId by mutableStateOf<Long?>(null)

    fun loadPlansForDate(date: LocalDate) {
        viewModelScope.launch {
            _plans.value = repo.getPlansForDate(date)
        }
    }

    fun addRecipeForDate(date: LocalDate) {
        selectedRecipeId?.let { recipeId ->
            val plannedRecipe = PlannedRecipe(recipeId = recipeId, date = date)
            viewModelScope.launch {
                repo.insert(plannedRecipe)
                loadPlansForDate(date)
            }
        }
    }

    fun deletePlannedRecipe(plannedRecipe: PlannedRecipeWithTask, date: LocalDate) {
        viewModelScope.launch {
            repo.delete(plannedRecipe.plannedRecipe)
            loadPlansForDate(date)
        }
    }


}
