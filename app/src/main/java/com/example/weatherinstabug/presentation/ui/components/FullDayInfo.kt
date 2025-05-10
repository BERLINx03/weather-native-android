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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weatherinstabug.R
import com.example.weatherinstabug.presentation.model.CurrentConditionsUi
import com.example.weatherinstabug.utils.getWindDirectionText

@Composable
fun FullDayInfo(
    modifier: Modifier = Modifier,
    currentConditions: CurrentConditionsUi
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(.5f)
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
        Spacer(Modifier.width(8.dp))
        Column(
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
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF6F92CC))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                InfoItem(title = "Humidity", value = "${currentConditions.humidity}%")
                HorizontalDivider()
                InfoItem(title = "Real feel", value = "${currentConditions.feelsLike.toInt()}°")
                HorizontalDivider()
                InfoItem(title = "UV", value = currentConditions.uvIndex.toString())
                HorizontalDivider()
                InfoItem(title = "Pressure", value = "${currentConditions.pressure.toInt()} mbar")
                HorizontalDivider()
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
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.White)
        Spacer(Modifier.weight(1f))
        Text(value, color = Color.White)
    }
}

@Composable
fun WindDirection(
    windDir: Double,
    windSpeed: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(bottom = 8.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6F92CC)
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
            ) {
                Text(
                    getWindDirectionText(windDir),
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                    lineHeight = MaterialTheme.typography.titleMedium.lineHeight
                )
                Text(
                    "Wind speed: $windSpeed km/h",
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.compass_icon),
                contentDescription = "Wind icon",
                tint = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
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
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6F92CC)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
            ) {
                Text(
                    "Sunrise: $sunrise",
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                )
                Text(
                    "Sunset: $sunset", color = Color.White,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.sunrise),
                contentDescription = "Sunrise icon",
                tint = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
            )
        }
    }
}