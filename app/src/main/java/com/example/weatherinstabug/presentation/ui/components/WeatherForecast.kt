package com.example.weatherinstabug.presentation.ui.components
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherinstabug.data.Day
import com.example.weatherinstabug.data.WeatherResponse
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/**
 * Main composable for the 5-day weather forecast
 */
@Composable
fun WeatherForecast(weatherResponse: WeatherResponse) {
    val forecastDays = weatherResponse.days.take(5)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "5-day forecast",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            DayForecastChart(forecastDays)
        }
    }
}

/**
 * Helper function to convert weather icons to appropriate colors
 */
fun getWeatherIconColor(icon: String): Color {
    return when {
        icon.contains("clear") || icon.contains("sunny") -> Color(0xFFE6C46C) // Golden yellow for clear/sunny
        icon.contains("cloudy") || icon.contains("partly-cloudy") -> Color(0xFFBBC1C7) // Gray for cloudy
        icon.contains("rain") -> Color(0xFF89CFF0) // Light blue for rain
        icon.contains("snow") -> Color(0xFFE0FFFF) // Light cyan for snow
        else -> Color(0xFFE6C46C) // Default to sunny
    }
}

/**
 * Helper function to format date string to readable format
 */
fun formatDate(dateString: String): Pair<String, String> {
    val date = LocalDate.parse(dateString)
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val dayMonth = "${date.monthValue}/${date.dayOfMonth}"
    return Pair(dayMonth, dayOfWeek)
}

/**
 * Helper function to get wind direction arrow
 */
fun getWindDirectionArrow(direction: String): String {
    return when (direction.uppercase()) {
        "N" -> "↑"
        "NE" -> "↗"
        "E" -> "→"
        "SE" -> "↘"
        "S" -> "↓"
        "SW" -> "↙"
        "W" -> "←"
        "NW" -> "↖"
        else -> "→"
    }
}

@Composable
fun DayForecastChart(days: List<Day>) {
    val dayCount = days.size
    val currentDay = 0 // Assuming the first day is the current day

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        // Day headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEachIndexed { index, day ->
                val (dayOfWeek, dayMonth) = formatDate(day.datetime)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(64.dp)
                        .let {
                            if (index == currentDay) it.background(
                                Color(0xFF202020),
                                RoundedCornerShape(8.dp)
                            ) else it
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = dayOfWeek,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = dayMonth,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Weather icon (sun, cloud, etc.)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(getWeatherIconColor(day.icon), RoundedCornerShape(50))
                    )
                }
            }
        }

        // Temperature chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 100.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val stepX = width / (dayCount - 1)

                // High temperature line
                val highPath = Path()
                val maxTemp = days.maxOf { it.tempmax }
                val minTemp = days.minOf { it.tempmin }
                val tempRange = maxTemp - minTemp

                days.forEachIndexed { index, day ->
                    val x = index * stepX
                    val y = height - (height * (day.tempmax - minTemp) / tempRange)

                    if (index == 0) {
                        highPath.moveTo(x.toFloat(), y.toFloat())
                    } else {
                        highPath.lineTo(x.toFloat(), y.toFloat())
                    }

                    // Temperature point
                    drawCircle(
                        color = Color.White,
                        radius = 5f,
                        center = Offset(x.toFloat(), y.toFloat())
                    )

                    // Temperature value
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "${day.tempmax.toInt()}°",
                            x,
                            y.toFloat() - 15,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.WHITE
                                textSize = 30f
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }

                // Draw the high temperature line
                drawPath(
                    path = highPath,
                    color = Color.White,
                    style = Stroke(width = 2f, cap = StrokeCap.Round)
                )

                // Low temperature line
                val lowPath = Path()

                days.forEachIndexed { index, day ->
                    val x = index * stepX
                    val y = height - (height * (day.tempmin - minTemp) / tempRange)

                    if (index == 0) {
                        lowPath.moveTo(x.toFloat(), y.toFloat())
                    } else {
                        lowPath.lineTo(x.toFloat(), y.toFloat())
                    }

                    // Temperature point
                    drawCircle(
                        color = Color.White,
                        radius = 5f,
                        center = Offset(x.toFloat(), y.toFloat())
                    )

                    // Temperature value
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "${day.tempmin.toInt()}°",
                            x,
                            y.toFloat() + 30,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.WHITE
                                textSize = 30f
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }

                // Draw the low temperature line
                drawPath(
                    path = lowPath,
                    color = Color.White,
                    style = Stroke(width = 2f, cap = StrokeCap.Round)
                )
            }
        }

        // Moon phase and wind info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            // Placeholder for moon phases
            // Note: Your data model doesn't have moon phase info,
            // so we're using a placeholder that looks similar to the image
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEach { _ ->
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFFD7BA76), RoundedCornerShape(50))
                            .padding(4.dp)
                    ) {
                        // This creates a crescent moon shape similar to the image
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color.Black, RoundedCornerShape(50))
                                .align(Alignment.CenterEnd)
                        )
                    }
                }
            }

            // Wind information
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEach { day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(64.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = getWindDirectionArrow(day.winddir),
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${day.windspeed.toInt()}km/h",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// Example usage in your app:
//
// @Composable
// fun WeatherScreen(weatherResponse: WeatherResponse) {
//     Column(
//         modifier = Modifier
//             .fillMaxSize()
//             .background(Color.Black)
//     ) {
//         WeatherForecast(weatherResponse)
//     }
// }