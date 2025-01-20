package org.ronil.hissab.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.ronil.hissab.models.ExpenseType
import org.ronil.hissab.ui.Typography
import org.ronil.hissab.utils.AppColors
import org.ronil.hissab.utils.LocalSnackBarProvider
import org.ronil.hissab.utils.getTextFiledColors
import org.ronil.hissab.viewmodels.UserDetailVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(userId: Int, finish: () -> Unit, viewmodel: UserDetailVM = koinViewModel()) {
    LaunchedEffect(Unit) {
        viewmodel.getAllTransactions(userId)
    }

    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                Icon(
                    Icons.Default.ArrowBack,
                    modifier = Modifier.padding(start = 10.dp).clickable {
                        finish()
                    },
                    contentDescription = "Back",
                    tint = AppColors.whiteColor
                )
            },
            actions = {
                Icon(
                    Icons.Filled.MoreVert,
                    modifier = Modifier.padding(end = 10.dp),
                    contentDescription = "Back",
                    tint = AppColors.whiteColor
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.accentColor),
            title = {
                Text(
                    "Ronil",
                    modifier = Modifier.padding(10.dp),
                    color = AppColors.whiteColor
                )
            })
    }, floatingActionButton = {
        FloatingActionButton(onClick = {

        }, modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = viewmodel.textVisible,
                    enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                ) {
                    Text("Add User", modifier = Modifier.padding(horizontal = 10.dp))
                }
                Icon(Icons.Default.Add, contentDescription = "for Adding the User")
            }
        }
    }) {
        if (viewmodel.showAddDialogue) {
            AddExpenseDialogue(viewmodel.amount, onAmountChange = {
                it.toDoubleOrNull()?.let { amount ->
                    viewmodel.amount = amount
                }
            }, {
                viewmodel.showAddDialogue = false
                viewmodel.amount = null
            }, {
                viewmodel.addExpense(userId, it)

            })
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExpenseDialogue(
    amount: Double?,
    onAmountChange: (String) -> Unit,
    cancel: () -> Unit,
    navigate: (ExpenseType) -> Unit
) {
    var selectedType by rememberSaveable { mutableStateOf(ExpenseType.GIVEN) }
    val snackBar = LocalSnackBarProvider.current
    BasicAlertDialog(
        modifier = Modifier.clip(RoundedCornerShape(20.dp))
            .background(AppColors.backgroundColor)
            .padding(10.dp),
        onDismissRequest = { cancel() }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 20.dp)
        ) {

            Text(
                "Add Expense",
                color = AppColors.accentColor,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp),
                style = Typography.titleLarge
            )

            TextField(
                value = amount.toString(),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp).apply {
                    getTextFieldModifier()
                },
                colors = getTextFiledColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                onValueChange = onAmountChange, label = {
                    Text("Enter Transaction Amount")
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButtonWithLabel(
                    text = ExpenseType.GIVEN.name.lowercase()
                        .replaceFirstChar { it.uppercaseChar() },
                    isSelected = selectedType == ExpenseType.GIVEN,
                    onClick = { selectedType = ExpenseType.GIVEN }
                )
                Spacer(modifier = Modifier.width(16.dp))
                RadioButtonWithLabel(
                    text = ExpenseType.TAKEN.name.lowercase()
                        .replaceFirstChar { it.uppercaseChar() },
                    isSelected = selectedType == ExpenseType.TAKEN,
                    onClick = { selectedType = ExpenseType.TAKEN }
                )
            }

            Button(modifier = Modifier.padding(vertical = 10.dp), onClick = {
                if (amount?.toString().isNullOrEmpty() || (amount != null && amount <= 0.0)) {
                    snackBar.showNegativeSnackBar("Please Enter a valid Amount")
                } else {
                    navigate(selectedType)
                }
            }) {
                Text("Add Entry")
            }

        }
    }

}

@Composable
private fun RadioButtonWithLabel(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null // Set to null to handle click on the entire Row
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}