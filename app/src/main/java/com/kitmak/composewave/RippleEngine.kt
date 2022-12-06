package com.kitmak.composewave

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

object RippleEngine {
    // Size
    const val N = 250
    // Scaling factor of pixel drawing position
    const val SCALE = 5
    // number of cols
    private const val COLS = N
    // number of rows
    private const val ROWS = N
    // Total number of pixels
    private const val MAX = ROWS * COLS
    // Factor of dampening, must be power of two, larger the power slow the ripple dampens.
    private const val DAMPENING = 64

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

    /**
     * Calls on pixel clicks, highlight the clicked and surrounding pixels in [prev] buffer
     *
     * @param offset coordinates of clicked pixels
     */
    fun onClick(offset: Offset) {
        if (offset.x < 0 || offset.y < 0) return

        val clickIndex = getIndex(offset.x.toInt() / SCALE, offset.y.toInt() / SCALE)

        if (clickIndex > MAX || clickIndex < 0) return

        prev[clickIndex] = 255f
        prev[clickIndex+N] = 255f
        prev[clickIndex-N] = 255f
        prev[clickIndex+1] = 255f
        prev[clickIndex-1] = 255f
    }

    /**
     * Converts 2d array indices to 1d index
     */
    private fun getIndex(x: Int, y: Int): Int {
        return x + y * COLS
    }

    /**
     * Updates state of pixel locations
     */
    fun evolve() {
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
