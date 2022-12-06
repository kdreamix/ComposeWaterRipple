package com.kitmak.composewave

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize

@SuppressLint("UnrememberedMutableState")
@Composable
fun WaterRipple(modifier: Modifier = Modifier, rippleEngine: RippleEngine = RippleEngine()) {
    var width by remember { mutableStateOf(250) }
    var height by remember { mutableStateOf(250) }

    rippleEngine.setWidth(width)
    rippleEngine.setHeight(height)

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis {
                rippleEngine.evolve()
            }
        }
    }

    Canvas(modifier = modifier) {
        width = size.height.toInt()
        height = size.height.toInt()

        rippleEngine.invalidator.apply {
            drawImage(rippleEngine.imageBitmap.asImageBitmap())
        }
    }
}




