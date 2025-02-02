package org.ronil.hissab.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import org.koin.compose.viewmodel.koinViewModel
import org.ronil.hissab.di.Log
import org.ronil.hissab.models.ExpenseModel
import org.ronil.hissab.models.ExpenseType
import org.ronil.hissab.ui.Typography
import org.ronil.hissab.utils.AppColors
import org.ronil.hissab.utils.LocalSnackBarProvider
import org.ronil.hissab.utils.formatDate
import org.ronil.hissab.utils.getTextFiledColors
import org.ronil.hissab.utils.toDate
import org.ronil.hissab.viewmodels.UserDetailVM
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: Int,
    userName: String,
    finish: () -> Unit,
    viewmodel: UserDetailVM = koinViewModel()
) {
    LaunchedEffect(Unit){
        viewmodel.userId=userId
    }


    val transactions by viewmodel.getUserTransactions(userId)
        .collectAsState(initial = emptyList())
    val netBalance by viewmodel.calculateNetBalance(userId)
        .collectAsState(initial = 0.0)

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Row(
                        modifier = Modifier
                            .clickable { finish() }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF007AFF) // iOS blue
                        )

                    }
                },
                title = {
                    Text(
                        userName.replaceFirstChar { it.uppercaseChar() },
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                },
//                actions = {
//                    IconButton(onClick = { /* Handle menu click */ }) {
//                        Icon(
//                            Icons.Default.MoreVert,
//                            contentDescription = "More options",
//                            tint = Color.Gray
//                        )
//                    }
//                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewmodel.isEdit = false
                    viewmodel.selectedExpenseId = null

                    viewmodel.showAddDialogue = true
                },
                containerColor = Color(0xFF007AFF),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add Transaction")
            }
        },
        containerColor = Color(0xFFF2F2F7) // iOS background gray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Settlement Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Settlement Amount",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        IconButton(onClick = { viewmodel.showDetails = !viewmodel.showDetails }) {
                            Icon(
                                if (viewmodel.showDetails) Icons.Default.KeyboardArrowUp
                                else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle details",
                                tint = Color(0xFF007AFF)
                            )
                        }
                    }

                    val amountColor = if (netBalance < 0) Color.Red else Color(0xFF34C759)
                    val amountText = if (netBalance < 0)
                        "You need to give ₹${abs(netBalance)}"
                    else
                        "You need to take ₹$netBalance"

                    Text(
                        amountText,
                        style = MaterialTheme.typography.headlineSmall,
                        color = amountColor,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    AnimatedVisibility(
                        visible = viewmodel.showDetails,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            Text(
                                "Total Transactions: ₹${viewmodel.totalTransaction}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                "Total Borrowed: ₹${viewmodel.borrowedAmount}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                "Total Given: ₹${viewmodel.lentAmount}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Transactions List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(transactions) { transaction ->
                    TransactionItem(transaction, onEdit = {
                        viewmodel.reason = it.reason
                        viewmodel.amount = it.transactionAmount.toString()
                        viewmodel.description = it.description
                        viewmodel.selectedDate = it.date.toDate()
                        viewmodel.selectedExpenseId = it.id
                        viewmodel.showAddDialogue = true
                        viewmodel.isEdit = true
                    }, onDelete = {
                        viewmodel.deleteExpense(it)

                    })
                }
                item {
                    Spacer(Modifier.height(50.dp))
                }
            }
        }

        // Add Transaction Dialog
        if (viewmodel.showAddDialogue) {
            AddEditExpenseDialogue(
                viewmodel = viewmodel,
                cancel = { viewmodel.showAddDialogue = false }
            ) {
                if (viewmodel.isEdit) {
                    viewmodel.editExpense(userId, it)

                } else {
                    viewmodel.addExpense(userId, it)
                }
                viewmodel.showAddDialogue = false
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: ExpenseModel, onEdit: (ExpenseModel) -> Unit,
    onDelete: (ExpenseModel) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val isGiven = transaction.type == ExpenseType.GIVEN
    val backgroundColor = if (isGiven) Color(0xFFFFEFEF) else Color(0xFFE5F8E5)
    val textColor = if (isGiven) Color(0xFFD32F2F) else Color(0xFF388E3C)

    Card(
        onClick = {
            showBottomSheet=true
        },
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 80.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.reason.replaceFirstChar { it.uppercaseChar() },
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )

                transaction.description?.let {
                    if (it.isNotBlank()) {
                        Text(
                            text = it.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Text(
                    text = transaction.date.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Text(
                text = "₹${transaction.transactionAmount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
    if (showBottomSheet) {
        TransactionActionsBottomSheet(
            transaction = transaction,
            onDismiss = { showBottomSheet = false },
            onEdit = {
                onEdit(transaction)
                showBottomSheet = false
            },
            onDelete = {
                onDelete(transaction)
                showBottomSheet = false
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionActionsBottomSheet(
    transaction: ExpenseModel,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Transaction Details Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = transaction.reason.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "₹${transaction.transactionAmount}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            HorizontalDivider()

            // Edit Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEdit() }
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color(0xFF007AFF),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Edit Transaction",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF007AFF),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // Delete Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDelete() }
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Delete Transaction",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditExpenseDialogue(
    viewmodel: UserDetailVM,
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
                if (viewmodel.isEdit) "Edit Expense" else "Add Expense",
                color = AppColors.accentColor,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp),
                style = Typography.titleLarge
            )

            TextField(
                value = viewmodel.amount ?: "",
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    .getTextFieldModifier(),
                colors = getTextFiledColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                onValueChange = {
                    Log.e(it)
                    it.trim().toDoubleOrNull() ?: return@TextField
                    it.trim().let {
                        viewmodel.amount = it
                    }
                },
                label = {
                    Text("Enter Transaction Amount")
                }
            )

            TextField(
                value = viewmodel.reason ?: "",
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    .getTextFieldModifier(),

                colors = getTextFiledColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = {
                    viewmodel.reason = it
                },
                label = {
                    Text("Enter a Reason")
                }
            )
            TextField(
                value = viewmodel.description ?: "",
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    .getTextFieldModifier(),

                colors = getTextFiledColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = {
                    viewmodel.description = it
                },
                label = {
                    Text("Enter a Comment(Optional)")
                }
            )
            TextField(
                value = viewmodel.selectedDate.formatDate() ?: "",
                shape = RoundedCornerShape(15.dp),
                enabled = false,
                textStyle = TextStyle(color = AppColors.textColor),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .getTextFieldModifier().focusable(false)
                    .clickable {
                        viewmodel.showDatePicker = true
                    }, colors = getTextFiledColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = {},
                readOnly = true,
                label = { Text("Date of Transaction", color = AppColors.textColor) }
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
                if (viewmodel.amount
                        .isNullOrEmpty() || ((viewmodel.amount?.toDoubleOrNull()
                        ?: 0.0) <= 0.0)
                ) {
                    snackBar.showNegativeSnackBar("Please Enter a valid Amount")
                } else if (viewmodel.reason.isNullOrEmpty()) {
                    snackBar.showNegativeSnackBar("Please Enter a Reason")

                } else {
                    navigate(selectedType)
                }
            }) {
                Text("Add Entry")
            }

        }
    }

    DatePicker(viewmodel)

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


@Composable
private fun DatePicker(viewmodel: UserDetailVM) {
    WheelDatePickerView(
        showDatePicker = viewmodel.showDatePicker,
        height = 150.dp,
        onDoneClick = {
            viewmodel.selectedDate = it
            viewmodel.showDatePicker = false
        },
        onDateChangeListener = {

        },
        onDismiss = {
            viewmodel.showDatePicker = false

        },

        startDate = viewmodel.selectedDate,
    )

}