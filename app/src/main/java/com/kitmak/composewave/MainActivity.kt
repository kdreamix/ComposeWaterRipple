package com.kitmak.composewave

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import com.kitmak.composewave.ui.theme.ComposeWaveTheme

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
                    LaunchedEffect(Unit) {
                        while (true) {
                            withFrameMillis {
                                WaterWave.doWave()
                            }
                        }
                    }

                    Box(contentAlignment = Alignment.Center, modifier = Modifier
                    //     .pointerInput(Unit) {
                    //         detectTapGestures {
                    //         Log.d("WATER", "click $it")
                    //         WaterWave.onClick(Offset(25f, 25f))
                    //     }
                    // }.drawBehind {
                    //         WaterWave.invalidator.apply {
                    //             drawImage(WaterWave.imageBitmap.asImageBitmap())
                    //         }
                    // }
                    ) {
                        WaterRipple()
                    }
                }
            }
        }
    }
}