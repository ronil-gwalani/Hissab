package org.ronil.hissab.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Stable
class ShackBarState {
    private var job: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val duration = 2500L
    var isVisible by mutableStateOf(false)
        private set

    var message: String by mutableStateOf("")

    var snackBarType by mutableStateOf(ShackBarType.POSITIVE)
        private set

    private fun show(message: String) {
        this.message = message
        isVisible = true
    }

    fun dismiss() {
        isVisible = false
    }

    fun showNegativeSnackBar(message: String) {
        job?.cancel()
        snackBarType = ShackBarType.NEGATIVE
        show(message)
        job = coroutineScope.launch {
            delay(duration)
            dismiss()
        }
    }

    fun showPositiveSnackBar(message: String) {
        job?.cancel()
        snackBarType = ShackBarType.POSITIVE
        show(message)
        job = coroutineScope.launch {
            delay(duration)
            dismiss()
        }
    }

    enum class ShackBarType {
        POSITIVE, NEGATIVE
    }

}

@Composable
fun rememberSnackBarState(): ShackBarState {
    val state = remember { ShackBarState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(Float.MAX_VALUE), // Ensure it's always on top
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            modifier = Modifier.zIndex(Float.MAX_VALUE), // Ensure it's always on top

            visible = state.isVisible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
                    .padding(top = 35.dp)
//                        .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .wrapContentHeight()
                    .background(
                        if (state.snackBarType == ShackBarState.ShackBarType.POSITIVE)
                            Color.Green
                        else
                            Color.Red
                    )
                    .padding(17.dp)
            ) {
                Text(
                    text = state.message,
                    style = TextStyle(
                        color = if (state.snackBarType == ShackBarState.ShackBarType.POSITIVE)
                            AppColors.textColor
                        else
                            AppColors.whiteColor,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

            }
        }
    }

    return state
}