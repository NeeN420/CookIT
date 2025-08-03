package cz.mendelu.pef.cookit.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredients",
    foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = ["id"],
        childColumns = ["taskId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("taskId")]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val amount: String,
    val unit: String,
    val taskId: Long
)