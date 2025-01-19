package org.ronil.hissab.utils

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object MyAnimation {

    fun myEnterAnimation(): EnterTransition {
        return slideInHorizontally(
            animationSpec = tween(350),
            initialOffsetX = {
                it
            }
        )

//        expandHorizontally(
//            animationSpec = tween(350),
//            expandFrom = Alignment.CenterHorizontally
//        )
    }


    fun myExitAnimation(): ExitTransition {
        return slideOutHorizontally(
            animationSpec = tween(350),
            targetOffsetX = {
                -it
            }
        )

//        return shrinkHorizontally(
//            animationSpec = tween(350),
//            shrinkTowards = Alignment.CenterHorizontally
//        )
    }


    fun mySlideIn(): EnterTransition {
        return slideInHorizontally(
            animationSpec = tween(350),
            initialOffsetX = {
                it
            }
        )
    }

    fun mySlideOut(): ExitTransition {
        return slideOutHorizontally(
            animationSpec = tween(350),
            targetOffsetX = {
                -it
            }
        )

    }
}