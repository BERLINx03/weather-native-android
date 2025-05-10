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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.weatherinstabug.presentation.ui.theme.CardBlue
import com.example.weatherinstabug.presentation.ui.theme.PrimaryBlue
import com.example.weatherinstabug.presentation.ui.theme.TextWhite
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
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(
            containerColor = CardBlue.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ){
                Icon(
                    painter = painterResource(id = R.drawable.hot),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(start = 8.dp),
                    tint = TextWhite
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "5-days Forecast",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = TextWhite
                )
            }
            for (day in days.take(3)) {
                DayItem(day)
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = TextWhite
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                ),
                onClick = onForecastClick
            ) {
                Text(
                    text = "5-day forecast",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
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
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 8.dp)
    ) {
        Image(
            painter = painterResource(id = getCurrentWeatherIcon(day.icon)),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(36.dp)
        )
        Text(
            text = parseDate(day.dateTime),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 12.dp),
            color = TextWhite
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = day.conditions,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = TextWhite.copy(alpha = 0.9f),
            modifier = Modifier.padding(end = 12.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "${day.tempMax.toInt()}°",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextWhite,
        )

        Text(
            " / ",
            fontSize = 20.sp,
            color = TextWhite.copy(alpha = 0.8f)
        )
        Text(
            text = "${day.tempMin.toInt()}° ",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = TextWhite.copy(alpha = 0.8f)
        )
    }
}

fun parseDate(date: String): String {
    val formatter = DateTimeFormatter.ofPattern("MM/dd")
    return LocalDate.parse(date).format(formatter)
}