package com.kitmak.composewave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kitmak.composewave.ui.theme.ComposeWaveTheme
import com.kitmak.composewave.ui.theme.OceanBlue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rippleEngine = RippleEngine(
            dampening = 32,
        )
        setContent {
            ComposeWaveTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier
                    ) {
                        val canvasModifier =
                            Modifier
                                .background(OceanBlue)
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        rippleEngine.onClick(it)
                                    }
                                }
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = {
                                            rippleEngine.onClick(it)
                                        },
                                        onDrag = { change, _ ->
                                            rippleEngine.onClick(change.position)
                                        }
                                    )

                                }

                        WaterRipple(canvasModifier, rippleEngine)
                    }
                }
            }
        }
    }
}