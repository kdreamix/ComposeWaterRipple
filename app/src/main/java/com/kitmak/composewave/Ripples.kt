package com.kitmak.composewave

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kitmak.composewave.WaterWave.COLS
import com.kitmak.composewave.WaterWave.imageBitmap
import com.kitmak.composewave.WaterWave.invalidator
import com.kitmak.composewave.WaterWave.onClick
import kotlin.random.Random

@SuppressLint("UnrememberedMutableState")
@Composable
fun WaterRipple(modifier: Modifier = Modifier) {

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis {
                WaterWave.doWave()
            }
        }
    }
    val size = with(LocalDensity.current) { (COLS * WaterWave.SCALE).toDp() }

    Canvas(modifier = Modifier
        .background(Color(0, 54, 119))
        .size(size)
        .pointerInput(Unit) {
            detectTapGestures {
                onClick(it)
            }
        }
        .pointerInput(Unit){
            detectDragGestures(
                onDragStart = {
                    onClick(it)
                },
                onDrag = { change, _ ->
                    onClick(change.position)
                }
            )

        }) {

        invalidator.apply {
            drawImage(imageBitmap.asImageBitmap())
        }

    }
}


object WaterWave {
    const val DAMPENING = 64
    const val AMPLITUDE = 3
    const val SCALE = 5
    const val N = 250
    const val COLS = N
    const val ROWS = N
    const val MAX = ROWS * COLS

    var invalidator by mutableStateOf(0)
    var curr = mutableListOf<Float>()
    var prev = mutableListOf<Float>()

    var imageBitmap: Bitmap by mutableStateOf(
        Bitmap.createBitmap(
            COLS * SCALE,
            ROWS * SCALE,
            Bitmap.Config.ARGB_8888
        )
    )

    init {
        curr.addAll(MutableList(ROWS * COLS) { 0f })
        prev.addAll(MutableList(ROWS * COLS) { 0f })
    }

    fun onClick(offset: Offset) {
        if (offset.x < 0 || offset.y < 0) return
        // val x = Random.nextInt(1, COLS)
        // val y = Random.nextInt(1, COLS)
//        Log.d("WATER", "xy:$x, $y")
        val clickIndex = getIndex(offset.x.toInt() / SCALE, offset.y.toInt() / SCALE)
        if (clickIndex > MAX || clickIndex < 0) return

        curr[clickIndex] = 255f
    }

    fun getIndex(x: Int, y: Int): Int {
        return x + y * COLS
    }

    fun doWave() {
        for (i in ROWS until COLS * COLS - ROWS) {
            curr[i] = (prev[i + 1] + prev[i - 1] + prev[i + COLS] + prev[i - COLS]) / 2f - curr[i]
            // Dampening
            curr[i] = curr[i] - curr[i] / DAMPENING
        }
        //swap
        prev = curr.apply { curr = prev }


        // rendering
        for (i in 1 until COLS) {
            for (j in 1 until ROWS) {
                val index = getIndex(i, j)
                val brightness = curr[index].toInt()
                imageBitmap.setPixel(
                    i * SCALE,
                    j * SCALE,
                    android.graphics.Color.rgb(brightness, brightness, brightness)
                )


            }
        }
        invalidator++
    }
}


