package cz.mendelu.pef.cookit.screens.addEditRecipe

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import cz.mendelu.pef.cookit.navigation.INavigationRouter
import cz.mendelu.pef.cookit.ui.theme.elements.BaseScreen
import java.io.File
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.vanpra.composematerialdialogs.*
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import cz.mendelu.pef.cookit.R


@Composable
fun AddEditRecipeScreen(
    navigation: INavigationRouter,
    id: Long?
) {
    val viewModel = hiltViewModel<AddEditRecipeViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.loadRecipe(id)
    }

    if (state.value.recipeSaved ){
        LaunchedEffect(state) {
            navigation.returnBack()
        }
    }

    if (state.value.recipeDeleted ){
        LaunchedEffect(state) {
            repeat(2) { navigation.returnBack() }

        }
    }




    BaseScreen(
        topBarText = stringResource(R.string.add_edit_title),
        showLoading = state.value.loading,
        onBackClick = { navigation.returnBack() },
        actions = {
            if (id != null) {
                val dialogState = rememberMaterialDialogState()

                IconButton(onClick = {
                    dialogState.show()
                }) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                }

                MaterialDialog(
                    dialogState = dialogState,
                    buttons = {
                        positiveButton(stringResource(R.string.yes)) {
                            viewModel.deleteRecipe()
                        }
                        negativeButton(stringResource(R.string.cancel))
                    }
                ) {
                    title(stringResource(R.string.delete_recipe))
                    message(stringResource(R.string.delete_recipe_dialogue))
                }
            }
        }
    ) {
        AddEditRecipeScreenContent(
            paddingValues = it,
            data = state.value,
            actions = viewModel,
            isEditMode = id != null
        )
    }



}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddEditRecipeScreenContent(
    paddingValues: PaddingValues,
    data: AddEditRecipeUIState,
    actions: AddEditRecipeActions,
    isEditMode: Boolean
) {
    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)


    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    val context = LocalContext.current
    val uri = remember { mutableStateOf<Uri?>(null) }

    // ✅ Unikátní název pro každý nový soubor
    val photoFile = remember {
        File(context.cacheDir, "recipe_photo_${System.currentTimeMillis()}.jpg").apply { createNewFile() }
    }

    val photoUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
    }

    LaunchedEffect(Unit) {
        uri.value = photoUri
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && uri.value != null) {
            actions.onImageCaptured(uri.value!!)
        }
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // ---------------- TITLE ----------------
        Text(stringResource(R.string.recipe_title), style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = data.task.Title,
            onValueChange = actions::onTitleChanged,
            isError = data.taskTextError != null,
            supportingText = {
                data.taskTextError?.let { Text(text = stringResource(it)) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // ---------------- PHOTO ----------------
        if (data.task.imageUri != null) {
            Text(stringResource(R.string.photo), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(data.task.imageUri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp)
            )
        }

        if (isEditMode) {
            Button(
                onClick = { cameraLauncher.launch(photoUri) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.change_add_photo))
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // ---------------- PROCESS ----------------
        Text(stringResource(R.string.preparation_procces), style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = data.task.Process,
            onValueChange = actions::onProcessChanged,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(bottom = 24.dp)
        )

        // ---------------- ADD INGREDIENT ----------------
        Text(stringResource(R.string.new_ingredient), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Název ingredience (max 30 znaků)
            OutlinedTextField(
                value = data.currentIngredient,
                onValueChange = {
                    if (it.length <= 30) actions.onCurrentIngredientChanged(it)
                },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.weight(2f),
                singleLine = true
            )

            // Množství (pouze čísla, max 8 znaků)
            OutlinedTextField(
                value = data.currentAmount,
                onValueChange = {
                    if (it.length <= 8 && it.all { c -> c.isDigit() }) {
                        actions.onCurrentAmountChanged(it)
                    }
                },
                label = { Text(stringResource(R.string.amount)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = data.ingredientAmountError != null,
                supportingText = {
                    data.ingredientAmountError?.let { Text(text = stringResource(it)) }
                }
            )


            // Dropdown na jednotku
            var expanded by remember { mutableStateOf(false) }
            val unitOptions = listOf(stringResource(R.string.g), stringResource(R.string.kg), stringResource(
                R.string.ml
            ),stringResource(R.string.l), stringResource(R.string.pcs))
            val selectedUnit = data.currentUnit

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp)
            ) {
                Surface(
                    tonalElevation = 0.dp,
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                        .height(56.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedUnit.isNotBlank()) selectedUnit else stringResource(R.string.unit),
                            color = if (selectedUnit.isNotBlank()) Color.Unspecified else Color.Gray
                        )
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Expand")
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    unitOptions.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit) },
                            onClick = {
                                actions.onCurrentUnitChanged(unit)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

// Tlačítko na přidání ingredience
        Button(
            onClick = actions::addIngredient,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.add_ingredient))
        }



        // ---------------- INGREDIENT LIST ----------------
        if (data.ingredients.isNotEmpty()) {
            Text(stringResource(R.string.ingredients), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            data.ingredients.forEachIndexed { index, ingredient ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${ingredient.name} – ${ingredient.amount} ${ingredient.unit}")
                    IconButton(onClick = { actions.removeIngredient(index) }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete ingredient")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // ---------------- SAVE BUTTON ----------------
        Button(
            onClick = actions::saveRecipe,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(stringResource(R.string.save_recipe))
        }
    }
}
