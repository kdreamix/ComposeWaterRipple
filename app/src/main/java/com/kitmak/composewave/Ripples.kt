package com.kitmak.composewave

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Canvas as Canvas2
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import com.kitmak.composewave.WaterWave.cols
import com.kitmak.composewave.WaterWave.onClick
import com.kitmak.composewave.WaterWave.rows
import kotlinx.coroutines.delay
import kotlin.random.Random

@SuppressLint("UnrememberedMutableState")
@Composable
fun WaterRipple(modifier: Modifier = Modifier) {


    LaunchedEffect(Unit) {

        while (true) {
            withFrameNanos {
                WaterWave.doWave()
            }
        }

    }
    Canvas(modifier = Modifier
        .background(Color.Black)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures {
                Log.d("WATER", "click $it")
                onClick(Offset(25f, 25f))
            }
        }) {

//        for (i in 1 until cols * cols){
////            val index = WaterWave.getIndex(i,j)
//            val color = WaterWave.curr[i].toInt() * 255
//            drawCircle(
//                Color(
//                    color,
//                    color,color
//                ), center = getXY(i), radius = 2f
//            )
//        }
        // Manually create a Bitmap

//        val canvasBitmap = Canvas2(imageBitmap)


//        for (i in 1 until cols) {
//            for (j in 1 until rows) {
//
//                val index = WaterWave.getIndex(i, j)
//                val color = WaterWave.curr[index].toInt() * 255
//                val i  = i*10
//                val j = j* 10
//                imageBitmap?.setPixel(i,j, android.graphics.Color.rgb(color,color,color))
//
//            }
//        }
        WaterWave.imageBitmap?.let {
            this.drawImage(it.asImageBitmap())
        }

    }
}


object WaterWave {
    const val damping = 0.9f
    val cols = 50
    val rows = 50

    var curr = mutableStateListOf<Float>()
    var prev = mutableStateListOf<Float>()
    var imageBitmap: Bitmap? by mutableStateOf(null)



    init {
        curr.addAll(MutableList(rows * cols) { 0f })
        prev.addAll(MutableList(rows * cols) { 0f })
    }

    fun onClick(offset: Offset) {
//        val x = Random.nextInt(1, cols)
//        val y = Random.nextInt(1, cols)
//        Log.d("WATER", "xy:$x, $y")
        val clickIndex = getIndex(offset.x.toInt(), offset.y.toInt())
        curr[clickIndex] = 255f
    }

    fun getIndex(x: Int, y: Int): Int {
        return x + y * cols
    }

    fun doWave() {

        for (i in rows until cols * cols - rows) {
            curr[i] = (prev[i + 1] + prev[i - 1] + prev[i + cols] + prev[i - cols]) / 2 - curr[i]
            curr[i] = curr[i] * damping
        }
        //swap
        val tmp = prev
        prev = curr
        curr = tmp

        imageBitmap = Bitmap.createBitmap(
            cols*10,
            rows*10,
            Bitmap.Config.ARGB_8888
        )
        for (i in 1 until cols) {
            for (j in 1 until rows) {

                val index = WaterWave.getIndex(i, j)
                val color = WaterWave.curr[index].toInt() * 255
                val i  = i*10
                val j = j* 10
                imageBitmap?.setPixel(i,j, android.graphics.Color.rgb(color,color,color))

            }
        }
    }
}


