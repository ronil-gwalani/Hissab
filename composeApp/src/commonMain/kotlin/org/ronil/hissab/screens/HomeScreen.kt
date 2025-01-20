package org.ronil.hissab.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import org.ronil.hissab.models.UserModel
import org.ronil.hissab.navigation.NavRouts
import org.ronil.hissab.ui.Typography
import org.ronil.hissab.utils.AppColors
import org.ronil.hissab.utils.LocalSnackBarProvider
import org.ronil.hissab.utils.getTextFiledColors
import org.ronil.hissab.viewmodels.HomeVM

@Composable
fun HomeScreen(viewmodel: HomeVM = koinViewModel(), onClick: (Int) -> Unit) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewmodel.showAddDialogue = true
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
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            if (viewmodel.showAddDialogue) {
                AddUSerDialogue(viewmodel.name, {
                    viewmodel.name = it
                }, viewmodel.phoneNum, {
                    viewmodel.phoneNum = it
                }, {
                    viewmodel.showAddDialogue = false
                }) {
                    viewmodel.addUser()
                    viewmodel.showAddDialogue = false
                }
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viewmodel.users) {
                    UserItem(it) {
                        onClick(it.id)
                    }
                }

            }
        }

    }

}


@Composable
private fun UserItem(user: UserModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable {
            onClick()
        }
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Icon Placeholder
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.firstOrNull()?.toString()?.uppercase() ?: "",
                    style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // User Name
                Text(
                    text = user.name.replaceFirstChar { it.uppercaseChar() },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Total Transactions
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.exchange),
                        contentDescription = "Total Transactions",
                        modifier = Modifier.size(15.dp)

                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Transactions: ₹${user.totalTransaction}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Current Status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.money),
                        contentDescription = "Current Status",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Balance: ₹${user.currentStatus}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }


            }

            Spacer(modifier = Modifier.width(16.dp))

            // Contact Button (if available)
            if (user.contactNum != null) {
                IconButton(onClick = { /* Add action here */ }) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Contact",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

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
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp).apply {
                    getTextFieldModifier()
                },
                colors = getTextFiledColors(),

                onValueChange = onNameChange, label = {
                    Text("Enter User's Name")
                }
            )
            TextField(
                value = phoneNum,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp).apply {
                    getTextFieldModifier()
                },
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
fun getTextFieldModifier(): Modifier {
    var focus by remember { mutableStateOf(false) }
    LaunchedEffect(focus) {
        Log.d(focus)
    }

    return Modifier
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
