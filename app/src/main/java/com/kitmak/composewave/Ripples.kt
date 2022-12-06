package com.kitmak.composewave

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap

@SuppressLint("UnrememberedMutableState")
@Composable
fun WaterRipple(modifier: Modifier = Modifier, rippleEngine: RippleEngine = RippleEngine()) {

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis {
                rippleEngine.evolve()
            }
        }
    }

    Canvas(modifier = modifier) {
        rippleEngine.invalidator.apply {
            drawImage(rippleEngine.imageBitmap.asImageBitmap())
        }
    }
}




