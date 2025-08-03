package cz.mendelu.pef.cookit.screens.addEditIngredients


data class AddEditIngredientUIState(
    val id: Long? = null,
    val name: String = "",
    val amount: String = "",
    val unit: String = "g",
    val nameError: Int? = null,
    val amountError: Int? = null,
    val loading: Boolean = false,
    val saved: Boolean = false
)
