package cz.mendelu.pef.cookit.screens.addEditIngredients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.pef.cookit.R
import cz.mendelu.pef.cookit.navigation.INavigationRouter
import cz.mendelu.pef.cookit.ui.theme.elements.BaseScreen

@Composable
fun AddEditIngredientScreen(
    navigation: INavigationRouter,
    id: Long?, // může být null pro přidání
) {
    val viewModel = hiltViewModel<AddEditIngredientViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.loadIngredient(id)
    }

    if (state.value.saved) {
        LaunchedEffect(Unit) {
            navigation.returnBack()
        }
    }

    val unitOptions = listOf(stringResource(R.string.g), stringResource(R.string.kg), stringResource(
        R.string.ml
    ),stringResource(R.string.l), stringResource(R.string.pcs))
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isUnitError = state.value.unit.isBlank()

    BaseScreen(
        topBarText = if (id == null) stringResource(R.string.add_ingredient) else "Edit Ingredient",
        showLoading = state.value.loading,
        onBackClick = { navigation.returnBack() }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.value.name,
                onValueChange = {
                    if (it.length <= 30) viewModel.onNameChanged(it)
                },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.value.nameError != null,
                supportingText = {
                    state.value.nameError?.let { Text(text = stringResource(it)) }
                },
                singleLine = true
            )

            OutlinedTextField(
                value = state.value.amount,
                onValueChange = {
                    if (it.length <= 8 && it.all(Char::isDigit)) {
                        viewModel.onAmountChanged(it)
                    }
                },
                label = { Text(stringResource(R.string.amount)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.value.amountError != null,
                supportingText = {
                    state.value.amountError?.let { Text(text = stringResource(it)) }
                },
                singleLine = true
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { expanded = true }
            ) {
                OutlinedTextField(
                    value = state.value.unit,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.unit)) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.clickable { expanded = true }
                        )
                    },
                    isError = isUnitError,
                    supportingText = {
                        if (isUnitError) Text(stringResource(R.string.please_select_a_unit))
                    }
                )

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
                                viewModel.onUnitChanged(unit)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = viewModel::saveIngredient,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }

