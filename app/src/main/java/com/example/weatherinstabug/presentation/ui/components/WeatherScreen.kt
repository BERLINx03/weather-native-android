package com.example.weatherinstabug.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.weatherinstabug.presentation.model.WeatherUi
import com.example.weatherinstabug.presentation.ui.theme.CardBlue
import com.example.weatherinstabug.presentation.ui.theme.DefaultGradient
import com.example.weatherinstabug.presentation.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    weatherUi: WeatherUi,
    onForecastClick: () -> Unit,
    onRefresh: () -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by rememberSaveable { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(key1 = true) {
        visible = true
    }

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
                    Brush.verticalGradient(
                        colors = DefaultGradient
                    )
                )
        ) {
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(1000)) + slideInVertically(
                        initialOffsetY = { -40 },
                        animationSpec = tween(1000)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val address = weatherUi.resolvedAddress
                        if (!address.contains(",")) {
                            Text(
                                text = address,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp,
                                color = TextWhite
                            )
                        } else {
                            Text(
                                text = weatherUi.timezone,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp,
                                color = TextWhite
                            )
                        }
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(1200)) + slideInVertically(
                        initialOffsetY = { 40 },
                        animationSpec = tween(1200)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(440.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${weatherUi.days[0].temp.toInt()}",
                            fontSize = 130.sp,
                            modifier = Modifier.padding(end = 16.dp),
                            color = TextWhite,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "°C",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .align(alignment = Alignment.CenterEnd)
                                .padding(end = 100.dp, bottom = 60.dp),
                            color = TextWhite
                        )
                        Row(
                            modifier = Modifier
                                .align(alignment = Alignment.BottomCenter)
                                .padding(bottom = 120.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = weatherUi.currentConditions.conditions,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(end = 16.dp),
                                color = TextWhite
                            )

                            Text(
                                text = "${weatherUi.days[0].tempMax.toInt()}°",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = TextWhite,
                                modifier = Modifier.padding(end = 4.dp)
                            )

                            Text(
                                "/",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 4.dp),
                                color = TextWhite
                            )
                            Text(
                                text = "${weatherUi.days[0].tempMin.toInt()}°",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = TextWhite,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        Card(
                            modifier = Modifier
                                .width(320.dp)
                                .align(alignment = Alignment.BottomCenter)
                                .clip(RoundedCornerShape(24.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = CardBlue.copy(alpha = 0.8f)
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.leaf),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .size(34.dp),
                                    tint = TextWhite
                                )
                                Text(
                                    text = weatherUi.description,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(12.dp),
                                    color = TextWhite,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
            
            item {
                Spacer(Modifier.height(60.dp))
            }
            
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(1500)) + slideInVertically(
                        initialOffsetY = { 100 },
                        animationSpec = tween(1500)
                    )
                ) {
                    FiveDaysForecastCard(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        days = weatherUi.days,
                        onForecastClick = {
                            onForecastClick()
                        }
                    )
                }
            }
            
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(1800)) + slideInVertically(
                        initialOffsetY = { 100 },
                        animationSpec = tween(1800)
                    )
                ) {
                    HourlyForecast(
                        hours = weatherUi.days[0].hours,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
            item {
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}