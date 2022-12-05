package com.kitmak.composewave

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
    val size = with(LocalDensity.current) { (COLS * 5).toDp() }

    Canvas(modifier = Modifier
        .background(Color.Black)
        .size(size)
        .pointerInput(Unit) {
            detectTapGestures {
                Log.d("WATER", "click $it")
                onClick(it)
            }
        }) {

        invalidator.apply {
            drawImage(imageBitmap.asImageBitmap())
        }

    }
}


object WaterWave {
    private const val DAMPENING = 0.98f
    private const val N = 250
    const val COLS = N
    private const val ROWS = N

    var invalidator by mutableStateOf(0)
    var curr = mutableListOf<Float>()
    var prev = mutableListOf<Float>()

    var imageBitmap: Bitmap by mutableStateOf(Bitmap.createBitmap(
        COLS * 5,
        ROWS * 5,
        Bitmap.Config.ARGB_8888
    ))

    init {
        curr.addAll(MutableList(ROWS * COLS) { 0f })
        prev.addAll(MutableList(ROWS * COLS) { 0f })
    }

    fun onClick(offset: Offset) {
       // val x = Random.nextInt(1, COLS)
       // val y = Random.nextInt(1, COLS)
//        Log.d("WATER", "xy:$x, $y")
        val clickIndex = getIndex(offset.x.toInt()/5, offset.y.toInt()/5)
        curr[clickIndex] = 255f
    }

    fun getIndex(x: Int, y: Int): Int {
        return x + y * COLS
    }

    fun doWave() {
        for (i in ROWS until COLS * COLS - ROWS) {
            curr[i] = (prev[i + 1] + prev[i - 1] + prev[i + COLS] + prev[i - COLS]) / 2 - curr[i]
            curr[i] = curr[i] * DAMPENING
        }

        //swap
        val tmp = prev
        prev = curr
        curr = tmp

        // rendering
        for (i in 1 until COLS) {
            for (j in 1 until ROWS) {
                val index = getIndex(i, j)
                val color = curr[index].toInt()
                val i = i * 5
                val j = j * 5
                imageBitmap.setPixel(i,j, android.graphics.Color.rgb(color,color,color))

            }
        }
        invalidator ++
    }
}


