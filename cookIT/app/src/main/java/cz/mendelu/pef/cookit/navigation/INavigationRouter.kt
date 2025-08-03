package cz.mendelu.pef.cookit.navigation

interface INavigationRouter {
    fun navigateToMyRecipes()
    fun navigateToAddRecipe()
    fun returnBack()
    fun navigateToSearch()
    fun navigateToAddEditRecipe(id: Long?)
    fun navigateToRecipeDetail(id: Long)
    fun navigateToShoppingList()
    fun navigateToAddEditIngridient(id: Long? = null)
    fun  navigateToCalendar()




}