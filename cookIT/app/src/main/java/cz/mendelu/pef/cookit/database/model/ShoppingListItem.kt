package cz.mendelu.pef.cookit.database.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list_items")
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val name: String,
    val amount: String? = null,
    val unit: String? = null,
    val checked: Boolean = false
)
