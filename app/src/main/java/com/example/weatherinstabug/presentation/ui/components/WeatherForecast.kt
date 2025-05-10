package com.example.weatherinstabug.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherinstabug.R
import com.example.weatherinstabug.presentation.model.CurrentConditionsUi
import com.example.weatherinstabug.presentation.model.DayUi
import com.example.weatherinstabug.presentation.model.WeatherUi
import com.example.weatherinstabug.presentation.ui.theme.DefaultGradient
import com.example.weatherinstabug.presentation.ui.theme.TextWhite
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeatherForecast(
    weatherUi: WeatherUi,
    onBackClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(key1 = true) {
        visible = true
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = DefaultGradient
                )
            )
    ) {
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(800)) + slideInVertically(
                    initialOffsetY = { -40 },
                    animationSpec = tween(800)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onBackClick() },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(TextWhite.copy(alpha = 0.2f))
                            .padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Back",
                            modifier = Modifier.size(28.dp),
                            tint = TextWhite
                        )
                    }
                    Spacer(modifier = Modifier.weight(.4f))
                    val address = weatherUi.resolvedAddress
                    if (!address.contains(",")) {
                        Text(
                            text = address,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            color = TextWhite
                        )
                    } else {
                        Text(
                            text = weatherUi.timezone,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            color = TextWhite
                        )
                    }
                    Text(
                        text = "GMT${weatherUi.tzOffset}",
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = TextWhite.copy(alpha = 0.8f),
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(1000)) + slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = tween(1000)
                )
            ) {
                Column {
                    Text(
                        text = "5-Day Weather Forecast",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = TextWhite,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = TextWhite.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                }
            }
        }
        
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(1200)) + slideInVertically(
                    initialOffsetY = { 60 },
                    animationSpec = tween(1200)
                )
            ) {
                Column {
                    for (day in weatherUi.days.slice(1..5)) {
                        DayWeatherItem(
                            day = day,
                            currentConditions = weatherUi.currentConditions
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = TextWhite.copy(alpha = 0.1f),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.padding(bottom = 24.dp))
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getDayFromDate(day.dateTime),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(end = 16.dp)
            )

            Spacer(modifier = Modifier.width(60.dp))

            Text(
                text = "${day.tempMax.toInt()}°",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(end = 8.dp)
            )

            Text(
                text = "/",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite.copy(alpha = 0.7f),
            )

            Text(
                text = "${day.tempMin.toInt()}°",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(getCurrentWeatherIcon(day.icon)),
                contentDescription = day.conditions,
                modifier = Modifier
                    .size(50.dp)
                    .padding(horizontal = 8.dp),
                tint = TextWhite
            )
        }

        if (expanded) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                DayInfo(
                    modifier = Modifier.padding(bottom = 16.dp),
                    currentConditions = currentConditions
                )
                HourlyForecast(
                    hours = day.hours
                )
            }
        }
    }
}

fun getDayFromDate(dateString: String): String {
    val date = LocalDate.parse(dateString)
    return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
}