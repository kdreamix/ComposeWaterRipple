package com.kitmak.composewave

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlin.math.max

/**
 * @param scale scaling factor of pixel drawing position
 * @param cols number of cols
 * @param rows number of rows
 * @param dampening dampening factor, must be power of two, larger the power slow the ripple dampens.
 */
class RippleEngine(
    private var scale: Int = Defaults.SCALE,
    var cols: Int = Defaults.COLS,
    var rows: Int = Defaults.ROWS,
    val dampening: Int = Defaults.DAMPENING,
) {
    private var running = false

    // Total number of pixels
    private val max: Int
        get() = rows * cols

    private object Defaults {
        const val SIZE = 250
        const val SCALE = 8
        const val COLS = SIZE
        const val ROWS = SIZE
        const val DAMPENING = 64
    }

    internal var invalidator by mutableStateOf(0)
    private var curr = MutableList(max) { 0f }
    private var prev = MutableList(max) { 0f }

    internal var imageBitmap: Bitmap by mutableStateOf(
        Bitmap.createBitmap(
            cols * scale,
            rows * scale,
            Bitmap.Config.ARGB_8888
        )
    )

    fun setWidth(width: Int) {
        cols = width / scale
        invalidate()
    }

    fun setHeight(height: Int) {
        rows = height / scale
        invalidate()
    }

    private fun invalidate(){
        curr = MutableList(max) { 0f }
        prev = MutableList(max) { 0f }
        imageBitmap =
            Bitmap.createBitmap(
                cols * scale,
                rows * scale,
                Bitmap.Config.ARGB_8888
            )
    }

    /**
     * Calls on pixel clicks, highlight the clicked and surrounding pixels in [prev] buffer
     *
     * @param offset coordinates of clicked pixels
     */
    fun onClick(offset: Offset) {
        running = true
        if (offset.x < 0 || offset.y < 0) return

        val clickIndex = getIndex(offset.x.toInt() / scale, offset.y.toInt() / scale)

        if (clickIndex > max || clickIndex < 0) return

        if (clickIndex + max(rows, cols) > max || clickIndex - max(rows, cols) < 0) return

        prev[clickIndex] = 255f
        prev[clickIndex + cols] = 255f
        prev[clickIndex - cols] = 255f
        prev[clickIndex + 1] = 255f
        prev[clickIndex - 1] = 255f
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
        if (!running) return
        for (i in cols until max - cols) {
            curr[i] = (prev[i + 1] + prev[i - 1] + prev[i + cols] + prev[i - cols]) / 2f - curr[i]
            // Dampening
            curr[i] = curr[i] - curr[i] / dampening
        }
        //swap buffer
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
