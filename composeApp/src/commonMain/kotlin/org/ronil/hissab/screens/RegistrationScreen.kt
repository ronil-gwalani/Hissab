package org.ronil.hissab.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hissab.composeapp.generated.resources.Res
import hissab.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.ronil.hissab.ui.Typography
import org.ronil.hissab.utils.AppColors
import org.ronil.hissab.utils.LocalSnackBarProvider
import org.ronil.hissab.utils.getTextFiledColors
import org.ronil.hissab.viewmodels.RegistrationVM

@Composable
fun RegistrationScreen(viewmodel: RegistrationVM = koinViewModel(), navigate: () -> Unit) {
    var focus by remember { mutableStateOf(false) }
    val snackBar = LocalSnackBarProvider.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(AppColors.backgroundColor)
            .padding(vertical = 20.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.logo), // Multiplatform resource reference
            contentDescription = "Logo of the app",
            modifier = Modifier.clip(RoundedCornerShape(100.dp))
        )
        Text(
            "Welcome to Hissab",
            color = AppColors.accentColor,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp),
            style = Typography.titleLarge
        )
        Text(
            "An Advance way to organize your expenses",
            color = AppColors.accentColor,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Normal,
            style = Typography.titleMedium
        )
        TextField(
            value = viewmodel.name,
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .border(
                    width = if (focus) 1.dp else 0.dp,
                    color = if (focus) AppColors.accentColor else Color.Transparent,
                    shape = RoundedCornerShape(15.dp),
                )
                .onFocusChanged { focusState ->
                    focus = focusState.isFocused
                },
            colors = getTextFiledColors(),

            onValueChange = {
                viewmodel.name = it
            }, label = {
                Text("Enter your Name")
            }
        )
        Button(onClick = {
            if (viewmodel.name.isEmpty()) {
                snackBar.showNegativeSnackBar("We need your name for Identification")
            } else {
                viewmodel.saveUser {
                    snackBar.showPositiveSnackBar("Thanks for the Information")
                }
                navigate()
            }
        }) {
            Text("Save My Name")
        }

    }

}

