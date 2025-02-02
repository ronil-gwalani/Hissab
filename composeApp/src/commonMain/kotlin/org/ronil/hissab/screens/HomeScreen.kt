package org.ronil.hissab.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import hissab.composeapp.generated.resources.Res
import hissab.composeapp.generated.resources.exchange
import hissab.composeapp.generated.resources.money
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.ronil.hissab.di.Log
import org.ronil.hissab.models.ExpenseModel
import org.ronil.hissab.models.UserModel
import org.ronil.hissab.navigation.NavRouts
import org.ronil.hissab.ui.Typography
import org.ronil.hissab.utils.AppColors
import org.ronil.hissab.utils.LocalSnackBarProvider
import org.ronil.hissab.utils.getTextFiledColors
import org.ronil.hissab.viewmodels.HomeVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewmodel: HomeVM = koinViewModel(), onClick: (UserModel) -> Unit) {
    val users by viewmodel.users.collectAsState()
    var showStatsDetail by remember { mutableStateOf(false) }

    // Calculate total statistics
    val totalStats = remember(users) {
        users.fold(Triple(0.0, 0.0, 0)) { acc, user ->
            Triple(
                acc.first + user.totalTransaction, // Total transactions
                acc.second + user.currentStatus,   // Net balance
                acc.third + 1                      // Total users
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Expense Manager",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { /* Add search functionality */ }) {
                        Icon(Icons.Default.Search, "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewmodel.showAddDialogue = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Add User")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp) // For FAB
            ) {
                // Stats Card
                item {
                    StatsCard(
                        totalStats = totalStats,
                        showDetail = showStatsDetail,
                        onToggleDetail = { showStatsDetail = !showStatsDetail }
                    )
                }

                // Users List
                items(
                    items = users,
                    key = { it.id } // Assuming UserModel has an id
                ) { user ->
                    UserItem(
                        user = user,
                        onClick = { onClick(user) }, onLongClick = {
                            viewmodel.selectedUser = user
                            viewmodel.showBottomSheet = true
                        }
                    )
                }
            }

            // Add User Dialog
            if (viewmodel.showAddDialogue) {
                AddUSerDialogue(
                    name = viewmodel.name,
                    onNameChange = { viewmodel.name = it },
                    phoneNum = viewmodel.phoneNum,
                    onPhoneNumChange = { viewmodel.phoneNum = it },
                    cancel = { viewmodel.showAddDialogue = false },
                    navigate = {
                        viewmodel.addUser()
                        viewmodel.showAddDialogue = false
                    }
                )
            }
            if (viewmodel.showBottomSheet) {
                ActionsBottomSheet(
                    name = viewmodel.name,
                    onDismiss = { viewmodel.showBottomSheet = false },
                    onCall = {
                        viewmodel.showBottomSheet=false
                       viewmodel.callUser(viewmodel.selectedUser?.contactNum)
                    },
                    onDelete = {
                        viewmodel.showBottomSheet=false
                        viewmodel.deleteUser()
                    })
            }
        }
    }
}

@Composable
private fun StatsCard(
    totalStats: Triple<Double, Double, Int>,
    showDetail: Boolean,
    onToggleDetail: () -> Unit
) {
    val (totalTransactions, netBalance, userCount) = totalStats

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Overview",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                IconButton(onClick = onToggleDetail) {
                    Icon(
                        if (showDetail) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle details"
                    )
                }
            }

            // Always visible stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.Person,
                    label = "Active Users",
                    value = userCount.toString()
                )
                StatItem(
                    icon = Icons.Default.Star,
                    label = "Net Balance",
                    value = "₹${netBalance.format(2)}"
                )
            }

            // Expandable details
            AnimatedVisibility(
                visible = showDetail,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                            alpha = 0.1f
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatItem(
                        icon = Icons.Default.Check,
                        label = "Total Transactions",
                        value = "₹${totalTransactions.format(2)}",
                        fullWidth = true
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
    fullWidth: Boolean = false
) {
    Row(
        modifier = Modifier
            .let { if (fullWidth) it.fillMaxWidth() else it }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun UserItem(user: UserModel, onClick: () -> Unit, onLongClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),

        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (user.currentStatus >= 0)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else
                            MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.firstOrNull()?.toString()?.uppercase() ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (user.currentStatus >= 0)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name.replaceFirstChar { it.uppercaseChar() },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Balance info with color coding
                Text(
                    text = if (user.currentStatus >= 0)
                        "You'll get ₹${user.currentStatus.format(2)}"
                    else
                        "You'll give ₹${(-user.currentStatus).format(2)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (user.currentStatus >= 0)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )

                Text(
                    text = "Total: ₹${user.totalTransaction.format(2)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }


        }
    }
}

// Helper function to format numbers
fun Double.format(digits: Int) = this.toString()
//    "%.${digits}f".format(this)

// Add this extension function for phone dialer
//fun Context.dialPhoneNumber(phoneNumber: String) {
//    val intent = Intent(Intent.ACTION_DIAL).apply {
//        data = Uri.parse("tel:$phoneNumber")
//    }
//    startActivity(intent)
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddUSerDialogue(
    name: String,
    onNameChange: (String) -> Unit,
    phoneNum: String,
    onPhoneNumChange: (String) -> Unit,
    cancel: () -> Unit,
    navigate: () -> Unit
) {

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
                "Add User",
                color = AppColors.accentColor,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp),
                style = Typography.titleLarge
            )

            TextField(
                value = name,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    .getTextFieldModifier(),
                colors = getTextFiledColors(),

                onValueChange = onNameChange, label = {
                    Text("Enter User's Name")
                }
            )
            TextField(
                value = phoneNum,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    .getTextFieldModifier(),

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = getTextFiledColors(),
                onValueChange = onPhoneNumChange, label = {
                    Text("Enter User's Contact Num (Optional)")
                }
            )
            Button(modifier = Modifier.padding(vertical = 10.dp), onClick = {
                if (name.isEmpty()) {
                    snackBar.showNegativeSnackBar("We need name for Identification")
                } else {
                    navigate()
                }
            }) {
                Text("Save Contact")
            }

        }
    }

}

@Composable
fun Modifier.getTextFieldModifier(): Modifier {
    var focus by remember { mutableStateOf(false) }
    return this
        .fillMaxWidth()
        .border(
            width = if (focus) 1.dp else 0.dp,
            color = if (focus) AppColors.accentColor else Color.Transparent,
            shape = RoundedCornerShape(15.dp),
        )
        .onFocusChanged { focusState ->
            focus = focusState.isFocused
        }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionsBottomSheet(
    name: String,
    onDismiss: () -> Unit,
    onCall: () -> Unit,
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
            Text(
                text = "Select Action",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )

            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCall() }
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call $name",
                    tint = Color(0xFF007AFF),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Call $name",
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
                    contentDescription = "Delete $name",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Delete",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
