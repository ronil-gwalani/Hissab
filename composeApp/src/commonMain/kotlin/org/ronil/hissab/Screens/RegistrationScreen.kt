package org.ronil.hissab.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import hissab.composeapp.generated.resources.Res
import hissab.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun RegistrationScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(20.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.logo), // Multiplatform resource reference
            contentDescription = "Logo of the app",
            modifier = Modifier.clip(RoundedCornerShape(100.dp))
        )
        Text("Welcome to Hissab", modifier = Modifier.padding(10.dp),)
    }

}
