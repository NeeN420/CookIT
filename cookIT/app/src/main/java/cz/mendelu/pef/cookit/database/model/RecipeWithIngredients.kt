package cz.mendelu.pef.cookit.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeWithIngredients(
    @Embedded val task: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val ingredients: List<Ingredient>
)
