package cz.mendelu.pef.cookit.screens.recipeDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import cz.mendelu.pef.cookit.R
import cz.mendelu.pef.cookit.navigation.INavigationRouter
import cz.mendelu.pef.cookit.screens.Calendar.CalendarViewModel
import cz.mendelu.pef.cookit.ui.theme.elements.BaseScreen
import cz.mendelu.pef.cookit.ui.theme.elements.CustomDataPickerDialog
import java.time.Instant
import java.time.ZoneId

@Composable
fun RecipeDetailScreen(
    id: Long,
    navigation: INavigationRouter
) {
    val viewModel = hiltViewModel<RecipeDetailViewModel>()
    val calendarViewModel = hiltViewModel<CalendarViewModel>()
    val recipe by viewModel.taskWithIngredients.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        viewModel.loadRecipe(id)
    }

    BaseScreen(
        topBarText = stringResource(R.string.recipe_detail),
        onBackClick = { navigation.returnBack() },
        showLoading = recipe == null,
        floatingActionButton = {
            recipe?.task?.id?.let { taskId ->
                FloatingActionButton(
                    onClick = { navigation.navigateToAddEditRecipe(taskId) }
                ) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                }
            }
        },
        bottomBar = null
    ) { padding ->
        recipe?.let { taskWithIngredients ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // ---------- Title ----------
                Text(
                    text = taskWithIngredients.task.Title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ---------- Image ----------
                if (taskWithIngredients.task.imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(taskWithIngredients.task.imageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_image), color = Color.DarkGray)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ---------- Ingredients ----------
                Text(stringResource(R.string.ingredients), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                if (taskWithIngredients.ingredients.isEmpty()) {
                    Text(text = stringResource(R.string.no_ingredients_added), color = Color.Gray)
                } else {
                    taskWithIngredients.ingredients.forEach {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )

                            Row {
                                it.amount?.let { amount ->
                                    Text(
                                        text = amount,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                it.unit?.let { unit ->
                                    Text(
                                        text = unit,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ---------- Add to shopping list ----------
                Button(
                    onClick = {
                        viewModel.addToShoppingList()
                        navigation.navigateToShoppingList()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.add_to_shopping_list))

                }

                // ---------- Plan recipe ----------
                Button(
                    onClick = {
                        showDatePicker = true
                    },
                    enabled = recipe?.task?.id != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.schedule_a_recipe))
                }

                if (showDatePicker) {
                    CustomDataPickerDialog(
                        onDateSelected = { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            calendarViewModel.selectedRecipeId = recipe!!.task.id
                            calendarViewModel.addRecipeForDate(date)
                            showDatePicker = false
                        },
                        onDismiss = { showDatePicker = false }
                    )
                }


                Spacer(modifier = Modifier.height(32.dp))
                Divider()
                Spacer(modifier = Modifier.height(24.dp))

                // ---------- Process ----------
                Text(stringResource(R.string.preparation_procces), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                if (taskWithIngredients.task.Process.isBlank()) {
                    Text(stringResource(R.string.no_preparation_procces), color = Color.Gray)
                } else {
                    Text(
                        text = taskWithIngredients.task.Process,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
