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
            withFrameNanos{
                WaterWave.doWave()
            }
        }
    }

    Canvas(modifier = Modifier
        .background(Color.Black)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures {

            }
        }) {
//        Log.d("WATER", "drawCircle")

        for (i in 1 until cols) {
            for (j in 1 until rows) {
                val index = WaterWave.getIndex(i,j)
                val color = WaterWave.curr[index].toInt() * 255
                drawCircle(
                    Color(
                        color,
                        color,color
                    ), center = Offset(i.toFloat() * 10, j.toFloat()* 10), radius = 1f
                )
//                drawCircle(Color.Blue, center = Offset(i.toFloat(),j.toFloat()), radius = 2f)
            }
        }
    }
}


object WaterWave {
    const val damping = 0.9f
    val cols = 50
    val rows = 50

    var curr = mutableStateListOf<Float>()
    var prev = mutableStateListOf<Float>()


    init {
        curr.addAll(MutableList(rows * cols) { 0f })
        prev.addAll(MutableList(rows * cols) { 0f })
//        pixels.addAll(MutableList(rows * cols) { 100f })
    }

    fun onClick() {
//        val x = Random.nextInt(1, cols)
//        val y = Random.nextInt(1, cols)
//        Log.d("WATER", "xy:$x, $y")
        val clickIndex = getIndex(25,25)
        prev[clickIndex] = 255f
    }

    fun getIndex(x: Int, y: Int): Int {
        return x + y * cols
    }

    fun getXY(index: Int): Pair<Int, Int> {
        val x = index % cols
        val y = index / cols
        return x to y
    }


    fun doWave() {

        for (i in rows until cols*cols - rows) {
//                val index = getIndex(i,)
//                val index1 = getIndex(i - 1, j)
//                val index2 = getIndex(i + 1, j)
//                val index3 = getIndex(i, j+1)
//                val index4 = getIndex(i, j-1)
//                Log.d("KIT", "index $index $index1 $index2 $index3 $index4")
                curr[i] = (
                        curr[i+1]
                                + curr[i-1]
                                + curr[i+cols]
                                + curr[i-cols]
                        ) / 2 - prev[i]
                curr[i] = curr[i] * damping
//                Log.d("WATER", "index:$index")

                // rendering
//                pixels[index] += curr[index] * 255

        }
        //swap
        val tmp = prev
        prev = curr
        curr = tmp
    }
}


