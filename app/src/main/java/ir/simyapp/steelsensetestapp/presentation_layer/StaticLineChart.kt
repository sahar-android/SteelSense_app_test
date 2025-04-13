package ir.simyapp.steelsensetestapp.presentation_layer

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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import ir.simyapp.steelsensetestapp.data_layer.DataSet
import java.time.LocalDateTime

/**
 * @Author: Sahar simyari
 * @Date: 4/11/2025
 */

private const val TAG = "StaticLineCharttest"
@Composable
fun StaticLineChartWithToggles(datasets: List<DataSet>) {
    // Variables for label placement specific to the dataset
    var labelPlaceX = 0f
    var labelPlaceY = 0f
    // Local visibility states for each line
    val lineVisibility = remember {
        mutableStateMapOf<String, Boolean>().apply {
            datasets.forEach {
                this[it.name] = true
            } // Default visibility: all lines visible
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {

            // Chart Canvas
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .weight(1f)) {
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
                        padding ,
                        y,
                        android.graphics.Paint().apply {
                            textAlign = android.graphics.Paint.Align.RIGHT
                            textSize = 30f
                            color = android.graphics.Color.WHITE
                        }
                    )
                }


                datasets.forEachIndexed { index, dataset ->
                    if (lineVisibility[dataset.name] == true) { // Check local visibility state
                        val path = Path()

                        dataset.dataPoints.forEachIndexed { i, point ->
                            val x = calculateX(point.timestamp, chartWidth, padding) // Adjusted X-axis scaling
                            val y = calculateY(point.value, chartHeight, padding,index) // Adjusted Y-axis scaling
                            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                            // Set label placement at a specific index
                            if (i == dataset.dataPoints.size / (index + 2)) {
                                labelPlaceX = x
                                labelPlaceY = y
                            }

                        }

                        // Draw the line
                        drawPath(
                            path = path,
                            color = colorList[index % colorList.size], // Assign color dynamically
                            style = Stroke(width = 4f)
                        )

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
                            labelPlaceX ,
                            labelPlaceY + labelOffsetY, // Apply vertical offset to avoid overlap,
                            android.graphics.Paint().apply {
                                textAlign = android.graphics.Paint.Align.RIGHT
                                textSize = 40f
                                color = android.graphics.Color.WHITE
                            }
                        )
                    }
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
                        modifier=Modifier.weight(1f),
                        checked = lineVisibility[dataset.name] ?: true,
                        onCheckedChange = { lineVisibility[dataset.name] = it }
                    )

                }
            }
        }
    }
}

// Sample colors for the lines
val lineColors = listOf(Color.Red, Color.Blue, Color.Green)


fun calculateX(timestamp: LocalDateTime, chartWidth: Float, padding: Float): Float {
    return padding + timestamp.minute.toFloat() / 60f * (chartWidth - 2 * padding)
}

fun calculateY(value: Float, chartHeight: Float, padding: Float, index: Int): Float {
   // return chartHeight - padding - (value / 100f * (chartHeight - 2 * padding))
    return chartHeight - padding - (value / 100f * (chartHeight - 2 * padding)) - index * 10f // Add unique vertical spacing
}








