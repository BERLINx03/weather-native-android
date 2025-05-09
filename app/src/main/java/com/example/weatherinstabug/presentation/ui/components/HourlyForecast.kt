package com.example.weatherinstabug.presentation.ui.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherinstabug.R
import com.example.weatherinstabug.data.Hour
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HourlyForecast(
    modifier: Modifier = Modifier,
    hours: List<Hour>
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .height(160.dp)
            .clip(MaterialTheme.shapes.large),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6F92CC)
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row {
                Icon(
                    painter = painterResource(R.drawable.time),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp)
                        .size(20.dp),
                    tint = Color.White
                )
                Text(
                    text = "24-hour forecast",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items(hours) { hour ->
                    HourlyForecastItem(
                        modifier = modifier.padding(start = 8.dp, bottom = 8.dp, top = 8.dp),
                        hour = hour
                    )
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun HourlyForecastItem(
    modifier: Modifier = Modifier,
    hour: Hour
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .width(72.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF4760D1), Color(0xFF89CFF0))
                    )
                )
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isSameHour(hour.datetime)) "Now" else takeHour(hour.datetime),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                Image(
                    painter = painterResource(id = getCurrentWeatherIcon(hour.icon)),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Row(

                ) {
                    Text(
                        text = "${hour.precipprob.toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFFFFFFFF),
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.water_7282919),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 3.dp, top = 3.dp)
                            .size(15.dp),
                        tint = Color.White
                    )
                }
                Text(
                    text = "${hour.temp.toInt()}°",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

}

fun isSameHour(targetTimeStr: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val targetTime = LocalTime.parse(targetTimeStr, formatter)
    val currentTime = LocalTime.now()

    return targetTime.hour == currentTime.hour
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun takeHour(timeStr: String): String {
    val hour = timeStr.substringBefore(":").toInt()
    val isPM = hour >= 12
    val hourIn12 = if (hour == 0 || hour == 12) 12 else hour % 12
    val period = if (isPM) "PM" else "AM"
    return "$hourIn12 $period"
}

@Preview
@Composable
fun HourlyForecastPreview() {
    HourlyForecast(
        hours =
            listOf(
                Hour("17:00:00", 12.3, 423.3, "clear", 12.0),
                Hour("11:00:00", 12.3, 423.3, "cloudy", 14.0),
                Hour("15:00:00", 12.3, 423.3, "cloudy", 31.2)
            )
    )
}