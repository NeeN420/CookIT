package cz.mendelu.pef.cookit.ui.theme.elements


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.cookit.navigation.Destination
import cz.mendelu.pef.cookit.navigation.INavigationRouter

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val destination: Destination
)

@Composable
fun BottomNavBar(
    currentRoute: String,
    navigation: INavigationRouter
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Search, Destination.SearchScreen),
        BottomNavItem("My Recipe", Icons.Default.Favorite, Destination.MyRecipeScreen),
        BottomNavItem("Shopping List", Icons.Default.DateRange, Destination.ShoppingListScreen),
        BottomNavItem( "Kalendář",Icons.Default.DateRange,Destination.Calendar)

    )

    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.destination.route,
                onClick = {
                    when (item.destination) {
                        is Destination.SearchScreen -> navigation.navigateToSearch()
                        is Destination.MyRecipeScreen -> navigation.navigateToMyRecipes()
                        is Destination.AddEditRecipeScreen -> navigation.navigateToAddRecipe()
                        is Destination.RecipeDetailScreen -> {}
                        is Destination.ShoppingListScreen -> navigation.navigateToShoppingList()
                       is Destination.Calendar -> navigation.navigateToCalendar()
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                    indicatorColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f)
                )
            )
        }
    }
}
