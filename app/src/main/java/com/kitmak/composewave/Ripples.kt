package com.kitmak.composewave

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.kitmak.composewave.WaterWave.cols
import com.kitmak.composewave.WaterWave.rows
import kotlinx.coroutines.delay
import kotlin.random.Random

@SuppressLint("UnrememberedMutableState")
@Composable
fun WaterRipple() {


    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            WaterWave.doWave()
        }
    }
    Canvas(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures {

            }
        }) {
        Log.d("WATER", "drawCircle")

        for (i in 1 until cols) {
            for (j in 1 until rows) {
                drawCircle(
                    Color(
                        WaterWave.pixels[i][j].toInt(),
                        WaterWave.pixels[i][j].toInt(),
                        WaterWave.pixels[i][j].toInt()
                    ), center = Offset(i.toFloat(), j.toFloat()), radius = 2f
                )
//                drawCircle(Color.Blue, center = Offset(i.toFloat(),j.toFloat()), radius = 2f)
            }
        }
    }
}


object WaterWave {
    const val damping = 098f
    val cols = 200
    val rows = 200

    var curr: MutableList<MutableList<Float>> by
    mutableStateOf(MutableList(WaterWave.cols) { MutableList(WaterWave.rows) { 100f } })


    var prev: MutableList<MutableList<Float>> by
    mutableStateOf(MutableList(WaterWave.cols) { MutableList(WaterWave.rows) { 100f } })


    var pixels: MutableList<MutableList<Float>> by
    mutableStateOf(MutableList(WaterWave.cols) { MutableList(WaterWave.rows) { 100f } })

    fun onClick(x: Int, y: Int) {
    }

    fun getIndex(x: Int, y: Int): Int {
        return x + y * cols
    }

    fun doWave() {
        // random pixel location
        val x = Random.nextInt(1, cols)
        val y = Random.nextInt(1, cols)
        Log.d("WATER", "xy:$x, $y")

        prev[x][y] = 255f

        for (i in 1 until cols - 1) {
            for (j in 1 until rows - 1) {
                curr[i][j] = (
                        curr[i - 1][j]
                                + curr[i + 1][j]
                                + curr[i][j - 1]
                                + curr[i][j + 1]
                        ) / 2 - prev[i][j]
                curr[i][j] = curr[i][j] * WaterWave.damping

                // rendering
                pixels[i][j] = curr[i][j] * 255
                val tmpPixels = pixels
                pixels = tmpPixels
            }
        }
        //swap
        val tmp = prev
        prev = (curr)
        curr = (tmp)
    }
}


