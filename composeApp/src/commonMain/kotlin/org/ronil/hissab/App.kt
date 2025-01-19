package org.ronil.hissab

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.ronil.hissab.navigation.SetUpNavGraph
import org.ronil.hissab.ui.HissabAppTheme

@Composable
@Preview
fun App() {
    HissabAppTheme {
        SetUpNavGraph()
    }
}