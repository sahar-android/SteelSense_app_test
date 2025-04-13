package ir.simyapp.steelsensetestapp.presentation_layer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.simyapp.steelsensetestapp.data_layer.DataSet
import java.time.LocalDateTime

/**
 * @Author: Sahar simyari
 * @Date: 4/13/2025
 */

@Composable
fun AnimatedLineChartWithTogglesOnVisibility(datasets: List<DataSet>) {
    // Variables for label placement specific to the dataset
    var labelPlaceX by remember { mutableStateOf(0f) }
    var labelPlaceY by remember { mutableStateOf(0f) }
    // Local visibility states for each line
    val lineVisibility = remember {
        mutableStateMapOf<String, MutableState<Boolean>>().apply {
            datasets.forEach {
                this[it.name] = mutableStateOf(true) // MutableState for each visibility
            }
        }
    }

    // Animation states for each dataset
    val animationProgress = remember {
        mutableStateMapOf<String, Animatable<Float, AnimationVector1D>>()
            .apply {
                datasets.forEach {
                    this[it.name] = Animatable(0f)
                }
            }
    }

    datasets.forEach { dataset ->
        val isVisibleState = lineVisibility[dataset.name]!!
        LaunchedEffect(isVisibleState.value) {
            if (isVisibleState.value) {
                animationProgress[dataset.name]?.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
                    initialVelocity = animationProgress[dataset.name]?.value ?: 0f // Continue from current progress if re-shown
                )
            } else {
                animationProgress[dataset.name]?.snapTo(0f) // Reset animation when hidden
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // Chart Canvas
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .weight(1f)
        ) {
            val chartWidth = size.width
            val chartHeight = size.height
            val padding = 30f // Padding for the chart

            // **Draw X Axis**
            drawLine(
                color = Color.White,
                start = Offset(padding, chartHeight - padding), // Bottom-left
                end = Offset(chartWidth - padding, chartHeight - padding), // Bottom-right
                strokeWidth = 6f
            )

            // **Draw Y Axis**
            drawLine(
                color = Color.White,
                start = Offset(padding, padding), // Top-left
                end = Offset(padding, chartHeight - padding), // Bottom-left
                strokeWidth = 3f
            )

            // **Draw Markers and Labels for X Axis**
            val xAxisInterval = (chartWidth - 2 * padding) / 6 // Divide X-axis into 6 sections
            for (i in 0..6) {
                val x = padding + i * xAxisInterval

                // Draw small marker
                drawLine(
                    color = Color.White,
                    start = Offset(x, chartHeight - padding),
                    end = Offset(x, chartHeight - padding + 10f), // A small vertical line
                    strokeWidth = 2f
                )

                // Draw label
                drawContext.canvas.nativeCanvas.drawText(
                    "${i * 10}", // Label values incrementally
                    x,
                    chartHeight - padding + 30f,
                    android.graphics.Paint().apply {
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 30f
                        color = android.graphics.Color.WHITE
                    }
                )
            }

            // **Draw Markers and Labels for Y Axis**
            val yAxisInterval = (chartHeight - 2 * padding) / 6 // Divide Y-axis into 6 sections
            for (i in 0..6) {
                val y = chartHeight - padding - i * yAxisInterval

                // Draw small marker
                drawLine(
                    color = Color.White,
                    start = Offset(padding - 10f, y),
                    end = Offset(padding, y), // A small horizontal line
                    strokeWidth = 2f
                )

                // Draw label
                drawContext.canvas.nativeCanvas.drawText(
                    "${i * 10}", // Label values incrementally
                    padding,
                    y,
                    android.graphics.Paint().apply {
                        textAlign = android.graphics.Paint.Align.RIGHT
                        textSize = 30f
                        color = android.graphics.Color.WHITE
                    }
                )
            }


            datasets.forEachIndexed { index, dataset ->
                val isVisible = lineVisibility[dataset.name]?.value ?: false
                if (isVisible) { // Check local visibility state
                    val path = Path()
                    val animatedProgress = animationProgress[dataset.name]?.value ?: 0f
                    val animatedDataPoints = dataset.dataPoints.take((dataset.dataPoints.size * animatedProgress).toInt())

                    animatedDataPoints.forEachIndexed { i, point ->
                        val x = calculateXAnim(point.timestamp, chartWidth, padding) // Adjusted X-axis scaling
                        val y = calculateYAnim(point.value, chartHeight, padding, index) // Adjusted Y-axis scaling
                        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        // Set label placement at a specific index within the visible points
                        if (i == animatedDataPoints.size / (index + 2) && animatedDataPoints.isNotEmpty()) {
                            labelPlaceX = x
                            labelPlaceY = y
                        }
                    }

                    // Draw the line
                    drawPath(
                        path = path,
                        color = lineColors[index % lineColors.size], // Assign color dynamically
                        style = Stroke(width = 4f)
                    )

                    if (animatedDataPoints.isNotEmpty()) {
                        val labelOffsetY = index * 20f // Add a unique offset based on the dataset index

                        // Text dimensions
                        val paint = android.graphics.Paint().apply {
                            textAlign = android.graphics.Paint.Align.RIGHT
                            textSize = 40f
                            color = android.graphics.Color.WHITE
                        }
                        val textWidth = paint.measureText(dataset.name)
                        val textHeight = paint.fontMetrics.run { bottom - top }

                        // Background rectangle coordinates
                        val rectLeft = labelPlaceX - textWidth - 20f // Add padding around the text
                        val rectTop = labelPlaceY + labelOffsetY - textHeight - 10f
                        val rectRight = labelPlaceX + 20f
                        val rectBottom = labelPlaceY + labelOffsetY + 30f

                        // Draw rounded rectangle as background
                        drawRoundRect(
                            color = Color.Black,
                            topLeft = Offset(rectLeft, rectTop),
                            size = Size(rectRight - rectLeft, rectBottom - rectTop),
                            cornerRadius = CornerRadius(16f, 16f)
                        )

                        // Draw border around the rectangle
                        drawRoundRect(
                            color = Color.White,
                            topLeft = Offset(rectLeft, rectTop),
                            size = Size(rectRight - rectLeft, rectBottom - rectTop),
                            cornerRadius = CornerRadius(16f, 16f),
                            style = Stroke(width = 2f)
                        )
                        // Draw label name
                        drawContext.canvas.nativeCanvas.drawText(
                            dataset.name, // Label values incrementally
                            labelPlaceX,
                            labelPlaceY + labelOffsetY, // Apply vertical offset to avoid overlap,
                            android.graphics.Paint().apply {
                                textAlign = android.graphics.Paint.Align.RIGHT
                                textSize = 40f
                                color = android.graphics.Color.WHITE
                            }
                        )
                    }
                }
                // The else block for handling non-visibility is now removed from Canvas
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .weight(0.25f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Toggles for Line Visibility
            datasets.forEach { dataset ->
                val isVisibleState = lineVisibility[dataset.name]!!
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Spacer(modifier = Modifier.weight(0.5f))
                    Text(
                        text = dataset.name,
                        modifier = Modifier.weight(0.5f),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Switch(
                        modifier = Modifier.weight(1f),
                        checked = isVisibleState.value,
                        onCheckedChange = { isVisibleState.value = it }
                    )
                }
            }
        }
    }
}

// Sample colors for the lines
val colorList = listOf(Color.Red, Color.Blue, Color.Green)



fun calculateXAnim(timestamp: LocalDateTime, chartWidth: Float, padding: Float): Float {
    return padding + timestamp.minute.toFloat() / 60f * (chartWidth - 2 * padding)
}

fun calculateYAnim(value: Float, chartHeight: Float, padding: Float, index: Int): Float {
    return chartHeight - padding - (value / 100f * (chartHeight - 2 * padding)) - index * 10f // Add unique vertical spacing
}





