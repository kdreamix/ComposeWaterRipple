package com.kitmak.composewave

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

/**
 * @param n size
 * @param scale scaling factor of pixel drawing position
 * @param cols number of cols
 * @param rows number of rows
 * @param dampening dampening factor, must be power of two, larger the power slow the ripple dampens.
 */
class RippleEngine(
    val n: Int = Defaults.N,
    val scale: Int = Defaults.SCALE,
    val cols: Int = Defaults.COLS,
    val rows: Int = Defaults.ROWS,
    val dampening: Int = Defaults.DAMPENING,
) {
    // Total number of pixels
    private val max: Int = rows * cols

    private object Defaults {
        const val N = 250
        const val SCALE = 5
        const val COLS = N
        const val ROWS = N
        const val DAMPENING = 64
    }

    var invalidator by mutableStateOf(0)
    var curr = mutableListOf<Float>()
    var prev = mutableListOf<Float>()

    var imageBitmap: Bitmap by mutableStateOf(
        Bitmap.createBitmap(
            cols * scale,
            rows * scale,
            Bitmap.Config.ARGB_8888
        )
    )

    init {
        curr.addAll(MutableList(max) { 0f })
        prev.addAll(MutableList(max) { 0f })
    }

    /**
     * Calls on pixel clicks, highlight the clicked and surrounding pixels in [prev] buffer
     *
     * @param offset coordinates of clicked pixels
     */
    fun onClick(offset: Offset) {
        if (offset.x < 0 || offset.y < 0) return

        val clickIndex = getIndex(offset.x.toInt() / scale, offset.y.toInt() / scale)

        if (clickIndex > max || clickIndex < 0) return

        if (clickIndex+n > max || clickIndex-n< 0) return

        prev[clickIndex] = 255f
        prev[clickIndex+n] = 255f
        prev[clickIndex-n] = 255f
        prev[clickIndex+1] = 255f
        prev[clickIndex-1] = 255f
    }

    /**
     * Converts 2d array indices to 1d index
     */
    private fun getIndex(x: Int, y: Int): Int {
        return x + y * cols
    }

    /**
     * Updates state of pixel locations
     */
    fun evolve() {
        for (i in rows until max - rows) {
            curr[i] = (prev[i + 1] + prev[i - 1] + prev[i + cols] + prev[i - cols]) / 2f - curr[i]
            // Dampening
            curr[i] = curr[i] - curr[i] / dampening
        }
        //swap
        prev = curr.apply { curr = prev }

        // rendering
        for (i in 1 until cols) {
            for (j in 1 until rows) {
                val index = getIndex(i, j)
                val brightness = curr[index].toInt()
                imageBitmap.setPixel(
                    i * scale,
                    j * scale,
                    android.graphics.Color.rgb(brightness, brightness, brightness)
                )
            }
        }
        invalidator++
    }

}
