package cz.mendelu.pef.cookit.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate

@Entity(
    tableName = "planned_recipes",
    foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = ["id"],
        childColumns = ["recipeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PlannedRecipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipeId: Long,
    val date: LocalDate
)

data class PlannedRecipeWithTask(
    @Embedded val plannedRecipe: PlannedRecipe,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "id"
    )
    val task: Recipe
)
