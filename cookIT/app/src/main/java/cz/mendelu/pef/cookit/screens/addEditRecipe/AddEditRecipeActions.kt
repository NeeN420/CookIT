package cz.mendelu.pef.cookit.screens.addEditRecipe

import android.net.Uri

interface AddEditRecipeActions {
    fun onTitleChanged(title: String)
    fun onProcessChanged(process: String)
    fun onCurrentIngredientChanged(name: String)
    fun addIngredient()
    fun saveRecipe()
    fun deleteRecipe()
    fun onCurrentAmountChanged(amount: String)
    fun removeIngredient(index: Int)
    fun onImageCaptured(uri: Uri)
    fun onCurrentUnitChanged(unit: String)

}