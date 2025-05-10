package com.example.weatherinstabug.presentation.ui.components

import android.util.Log.d
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherinstabug.R
import com.example.weatherinstabug.data.WeatherResponse
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    weatherResponse: WeatherResponse,
    onForecastClick: () -> Unit,
    onRefresh: () -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by rememberSaveable { mutableStateOf(false) }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = pullToRefreshState,
        onRefresh = { onRefresh() },
        isRefreshing = isRefreshing,
    ) {
        LazyColumn(
            modifier = Modifier
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
                        .padding(22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val address = weatherResponse.resolvedAddress
                    if (!address.contains(",")) {
                        Text(
                            text = address,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = weatherResponse.timezone,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(440.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${weatherResponse.days[0].temp.toInt()}",
                        fontSize = 120.sp,
                        modifier = Modifier.padding(end = 16.dp),
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "°C",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterEnd)
                            .padding(end = 100.dp, bottom = 60.dp),
                        color = Color.White
                    )
                    Row(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomCenter)
                            .padding(bottom = 120.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = weatherResponse.currentConditions.conditions,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 12.dp),
                            color = Color.White
                        )

                        Text(
                            text = "${weatherResponse.days[0].tempmax.toInt()}°",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier.padding(end = 4.dp)
                        )

                        Text(
                            "/",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            color = Color.White
                        )
                        Text(
                            text = "${weatherResponse.days[0].tempmin.toInt()}°",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    Card(
                        modifier = Modifier
                            .width(290.dp)
                            .align(alignment = Alignment.BottomCenter)
                            .clip(MaterialTheme.shapes.extraLarge),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF6F92CC)
                        ),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.leaf),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(30.dp),
                                tint = Color.White
                            )
                            Text(
                                text = weatherResponse.description,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(12.dp),
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2
                            )
                        }
                    }

                }
            }
            item {
                Spacer(Modifier.height(80.dp))
            }
            item {
                FiveDaysForecastCard(
                    modifier = Modifier.padding(16.dp),
                    days = weatherResponse.days,
                    onForecastClick = {
                        onForecastClick()
                    }
                )
            }
            item {
                HourlyForecast(
                    hours = weatherResponse.days[0].hours,
                    modifier = Modifier.padding(12.dp)
                )
            }
            item {
                FullDayInfo(
                    modifier = Modifier.padding(12.dp),
                    currentConditions = weatherResponse.currentConditions
                )
            }
        }
    }
}