package com.kitmak.composewave

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*

@SuppressLint("UnrememberedMutableState")
@Composable
fun WaterRipple(modifier: Modifier = Modifier) {

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis {
                RippleEngine.evolve()
            }
        }
    }

    Canvas(modifier = modifier) {
        RippleEngine.invalidator.apply {
            drawImage(RippleEngine.imageBitmap.asImageBitmap())
        }
    }
}




