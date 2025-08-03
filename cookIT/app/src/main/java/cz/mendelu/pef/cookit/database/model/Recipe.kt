package cz.mendelu.pef.cookit.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    var Title: String,
    var Process: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var taskState: Boolean = false,
    val imageUri: String? = null
)

