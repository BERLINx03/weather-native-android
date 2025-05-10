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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherinstabug.R
import com.example.weatherinstabug.presentation.model.HourUi
import com.example.weatherinstabug.presentation.ui.theme.CardBlue
import com.example.weatherinstabug.presentation.ui.theme.DefaultGradient
import com.example.weatherinstabug.presentation.ui.theme.PrimaryBlue
import com.example.weatherinstabug.presentation.ui.theme.TextWhite
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HourlyForecast(
    modifier: Modifier = Modifier,
    hours: List<HourUi>
) {
    Card(
        modifier = modifier
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(
            containerColor = CardBlue.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.time),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp),
                    tint = TextWhite
                )
                Text(
                    text = "24-hour forecast",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 12.dp),
                    color = TextWhite,
                    fontWeight = FontWeight.SemiBold
                )
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(hours) { hour ->
                    HourlyForecastItem(
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
    hour: HourUi
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .width(75.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = DefaultGradient
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
                    text = if (isSameHour(hour.dateTime)) "Now" else takeHour(hour.dateTime),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextWhite,
                    fontWeight = FontWeight.SemiBold
                )
                Image(
                    painter = painterResource(id = getCurrentWeatherIcon(hour.icon)),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${hour.precipProb.toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextWhite,
                        modifier = Modifier.padding(bottom = 4.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.water_7282919),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 3.dp, bottom = 4.dp)
                            .size(16.dp),
                        tint = TextWhite
                    )
                }
                Text(
                    text = "${hour.temp.toInt()}°",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
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