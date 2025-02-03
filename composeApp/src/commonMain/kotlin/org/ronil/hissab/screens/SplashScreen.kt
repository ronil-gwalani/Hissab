package org.ronil.hissab.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import hissab.composeapp.generated.resources.Res
import hissab.composeapp.generated.resources.logo

import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.ronil.hissab.utils.AppColors

@Composable
fun SplashScreen(
    navigate: suspend () -> Unit,
) {

    val scale = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            scale.animateTo(
                targetValue = 1.5f,
                animationSpec = tween(
                    durationMillis = 2500,
                    easing = { t -> overshootInterpolation(t, 1.5f) }
                )
            )
            navigate()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
//            .background(AppColors.accentColor),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(Res.drawable.logo), // Multiplatform resource reference
            contentDescription = "Logo of the app",
            modifier = Modifier
                .scale(scale.value)
                .clip(RoundedCornerShape(100.dp)),
            contentScale = ContentScale.Fit
        )
    }


}

inline fun overshootInterpolation(t: Float, tension: Float): Float {
    return (t - 1).let { it * it * ((tension + 1) * it + tension) + 1 }
}