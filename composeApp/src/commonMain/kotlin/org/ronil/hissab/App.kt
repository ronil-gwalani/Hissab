package org.ronil.hissab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import hissab.composeapp.generated.resources.Res
import hissab.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import org.ronil.hissab.di.Log
import org.ronil.hissab.utils.getCurrentDate
import org.ronil.hissab.utils.getCurrentTime
import org.ronil.hissab.viewmodels.TestingVM

@Composable
@Preview
fun App(vm: TestingVM = koinViewModel()) {
    val scope = rememberCoroutineScope()
    MaterialTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                scope.launch {
                    println(
                        vm.addEntry()
                    )
                }
            }) {
                Text("Click me!")
                Log.e(
                    getCurrentDate()   + getCurrentTime()
                )

            }

        }
    }
}