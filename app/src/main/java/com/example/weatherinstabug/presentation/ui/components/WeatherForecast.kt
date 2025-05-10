package com.example.weatherinstabug.presentation.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherinstabug.R
import com.example.weatherinstabug.presentation.model.CurrentConditionsUi
import com.example.weatherinstabug.presentation.model.DayUi
import com.example.weatherinstabug.presentation.model.WeatherUi
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeatherForecast(
    weatherUi: WeatherUi,
    onBackClick: () -> Unit
) {

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF4760D1), Color(0xFF89CFF0))
                )
            )
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBackClick() },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(.6f))
                val address = weatherUi.resolvedAddress
                if (!address.contains(",")) {
                    Text(
                        text = address,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = weatherUi.timezone,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
                Text(
                    text = "GMT${weatherUi.tzOffset}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 12.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        item {
            for (day in weatherUi.days.slice(1..5)) {
                DayWeatherItem(
                    day = day,
                    currentConditions = weatherUi.currentConditions
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
            }
        }
    }

}

@Composable
fun DayWeatherItem(
    modifier: Modifier = Modifier,
    day: DayUi,
    currentConditions: CurrentConditionsUi
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getDayFromDate(day.dateTime),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )

            Spacer(modifier = Modifier.width(60.dp))

            Text(
                text = "${day.tempMax.toInt()}°",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )

            Text(
                text = " / ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Text(
                text = "${day.tempMin.toInt()}°",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(getCurrentWeatherIcon(day.icon)),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(horizontal = 12.dp),
                tint = Color.White
            )
        }

        if (expanded) {
            Column {
                DayInfo(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                    currentConditions = currentConditions
                )
                HourlyForecast(
                    hours = day.hours,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                )
            }
        }
    }
}

fun getDayFromDate(dateString: String): String {
    val date = LocalDate.parse(dateString)
    return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()).substring(0..2)
}