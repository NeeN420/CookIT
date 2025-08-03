package cz.mendelu.pef.cookit.screens.shoppingList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.pef.cookit.navigation.INavigationRouter
import cz.mendelu.pef.cookit.ui.theme.elements.BaseScreen
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import cz.mendelu.pef.cookit.R
import cz.mendelu.pef.cookit.navigation.Destination
import cz.mendelu.pef.cookit.ui.theme.elements.BottomNavBar
import cz.mendelu.pef.cookit.ui.theme.elements.PlaceholderScreenContent


@Composable
fun ShoppingListScreen(
    navigation: INavigationRouter,
    viewModel: ShoppingListViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()

    BaseScreen(
        topBarText = stringResource(R.string.shopping_list),
        onBackClick = { navigation.returnBack() },
        actions = {
            IconButton(onClick = { viewModel.removeCheckedItems() }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete selected")
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigation.navigateToAddEditIngridient(null)
            }) {
                Icon(Icons.Outlined.Add, contentDescription = "Add")
            }
        },

        bottomBar = {
            BottomNavBar(
                currentRoute = Destination.ShoppingListScreen.route,
                navigation = navigation
            )
        },
        placeholderScreenContent = if (items.isEmpty()) {
            PlaceholderScreenContent(
                text = stringResource(R.string.you_have_no_ingredients),
                image = R.drawable.undraw_empty_cart_574u
            )
        } else null
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.checked)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else
                            MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (!item.amount.isNullOrBlank() || !item.unit.isNullOrBlank()) {
                                val (displayAmount, displayUnit) = when (item.unit) {
                                    "g" -> if ((item.amount?.toDoubleOrNull() ?: 0.0) >= 1000)
                                        String.format("%.2f", (item.amount?.toDoubleOrNull() ?: 0.0) / 1000) to "kg"
                                    else item.amount to "g"

                                    "ml" -> if ((item.amount?.toDoubleOrNull() ?: 0.0) >= 1000)
                                        String.format("%.2f", (item.amount?.toDoubleOrNull() ?: 0.0) / 1000) to "l"
                                    else item.amount to "ml"

                                    else -> item.amount to item.unit
                                }

                                Text(
                                    text = "$displayAmount $displayUnit",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                            }
                        }
                        Checkbox(
                            checked = item.checked,
                            onCheckedChange = { viewModel.toggleCheck(item) }
                        )
                    }
                }

            }
        }
    }
}
