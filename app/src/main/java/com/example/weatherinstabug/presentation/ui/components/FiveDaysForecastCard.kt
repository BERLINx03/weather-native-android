package com.example.weatherinstabug.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherinstabug.R
import com.example.weatherinstabug.presentation.model.DayUi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FiveDaysForecastCard(
    modifier: Modifier = Modifier,
    days: List<DayUi> = emptyList(),
    onForecastClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6F92CC)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.hot),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .padding(start = 8.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "5-days Forecast",
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            for (day in days.take(3)) {
                DayItem(day)
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor =Color(0xFF4760D1),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = onForecastClick
            ) {
                Text(text = "5-day forecast")
            }
        }
    }
}

@Composable
fun DayItem(day: DayUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 2.dp)
    ) {
        Image(
            painter = painterResource(id = getCurrentWeatherIcon(day.icon)),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 12.dp, end = 12.dp, bottom = 12.dp)
                .size(30.dp)
        )
        Text(
            text = parseDate(day.dateTime),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 12.dp),
            color = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = day.conditions,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(end = 12.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "${day.tempMax.toInt()}°",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.White,
        )

        Text(
            " / ",
            fontSize = 20.sp,
            color = Color.White
        )
        Text(
            text = "${day.tempMin.toInt()}° ",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

fun parseDate(date: String): String {
    val formatter = DateTimeFormatter.ofPattern("MM/dd")
    return LocalDate.parse(date).format(formatter)
}