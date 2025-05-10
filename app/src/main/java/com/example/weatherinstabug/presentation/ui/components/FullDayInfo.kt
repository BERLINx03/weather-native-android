package com.example.weatherinstabug.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherinstabug.R
import com.example.weatherinstabug.presentation.model.CurrentConditionsUi
import com.example.weatherinstabug.presentation.ui.theme.CardBlue
import com.example.weatherinstabug.presentation.ui.theme.TextWhite
import com.example.weatherinstabug.utils.getWindDirectionText

@Composable
fun FullDayInfo(
    modifier: Modifier = Modifier,
    currentConditions: CurrentConditionsUi
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WindDirection(
                windDir = currentConditions.windDir,
                windSpeed = currentConditions.windSpeed,
                modifier = Modifier.fillMaxHeight()
            )

            SunriseSunset(
                sunrise = currentConditions.sunrise,
                sunset = currentConditions.sunset
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            DayInfo(currentConditions = currentConditions)
        }
    }
}

@Composable
fun DayInfo(
    currentConditions: CurrentConditionsUi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Box(
            modifier = Modifier
                .background(CardBlue.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Weather Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                HorizontalDivider(
                    color = TextWhite.copy(alpha = 0.2f),
                    thickness = 1.dp
                )
                
                InfoItem(title = "Humidity", value = "${currentConditions.humidity}%")
                HorizontalDivider(color = TextWhite.copy(alpha = 0.1f))
                InfoItem(title = "Real feel", value = "${currentConditions.feelsLike.toInt()}°")
                HorizontalDivider(color = TextWhite.copy(alpha = 0.1f))
                InfoItem(title = "UV Index", value = currentConditions.uvIndex.toString())
                HorizontalDivider(color = TextWhite.copy(alpha = 0.1f))
                InfoItem(title = "Pressure", value = "${currentConditions.pressure.toInt()} mbar")
                HorizontalDivider(color = TextWhite.copy(alpha = 0.1f))
                InfoItem(title = "Chance of rain", value = "${currentConditions.precipProb}%")
            }
        }
    }
}

@Composable
fun InfoItem(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, 
            color = TextWhite,
            fontSize = 16.sp
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = value, 
            color = TextWhite,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun WindDirection(
    windDir: Double,
    windSpeed: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBlue.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
            ) {
                Text(
                    getWindDirectionText(windDir),
                    color = TextWhite,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Wind speed: $windSpeed km/h",
                    color = TextWhite.copy(alpha = 0.9f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.compass_icon),
                contentDescription = "Wind direction",
                tint = TextWhite,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(42.dp)
            )
        }
    }
}

@Composable
fun SunriseSunset(
    sunrise: String,
    sunset: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBlue.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
            ) {
                Text(
                    "Sunrise: $sunrise",
                    color = TextWhite,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Sunset: $sunset", 
                    color = TextWhite.copy(alpha = 0.9f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.sunrise),
                contentDescription = "Sunrise and sunset",
                tint = TextWhite,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(42.dp)
            )
        }
    }
}