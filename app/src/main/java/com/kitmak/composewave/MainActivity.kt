package com.kitmak.composewave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import com.kitmak.composewave.ui.theme.ComposeWaveTheme
import com.kitmak.composewave.ui.theme.OceanBlue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeWaveTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier
                    ) {
                        val size = with(LocalDensity.current) { (RippleEngine.N * RippleEngine.SCALE).toDp() }

                        val canvasModifier =
                            Modifier
                                .background(OceanBlue)
                                .size(size)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        RippleEngine.onClick(it)
                                    }
                                }
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = {
                                            RippleEngine.onClick(it)
                                        },
                                        onDrag = { change, _ ->
                                            RippleEngine.onClick(change.position)
                                        }
                                    )

                                }
                        WaterRipple(canvasModifier)
                    }
                }
            }
        }
    }
}