# Compose Water Ripple

An implementation of famous Hugo Elias water algorithm in Jetpack compose.

### Usage

```kotlin
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
                .width(400.dp)
                .height(400.dp)
                .pointerInput(Unit) {
                    detectTapGestures {
                        rippleEngine.onClick(it)
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            rippleEngine.onClick(it) },
                        onDrag = { change, _ ->
                            rippleEngine.onClick(change.position)
                        }
                    )

                }

        WaterRipple(canvasModifier, rippleEngine)
    }
}
```

### Demo

![This is a demo](ripple_demo.webm)


## Acknowledgments

* [Hugo Elias blog](https://web.archive.org/web/20160418004149/http://freespace.virgin.net/hugo.elias/graphics/x_water.htm)
* [Haska talks AI video](https://www.youtube.com/watch?v=2aQYlkpuBrA)
