package com.example.nammahasiru.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nammahasiru.ui.theme.EarthBrown
import com.example.nammahasiru.ui.theme.GreenPrimary
import com.example.nammahasiru.ui.theme.GreenSecondary
import com.example.nammahasiru.ui.theme.GreenTertiary
import com.example.nammahasiru.viewmodel.PlantViewModel

@Composable
fun DashboardScreen(viewModel: PlantViewModel) {
    val totalCount by viewModel.totalCount.collectAsState(initial = 0)
    val survivedCount by viewModel.survivedCount.collectAsState(initial = 0)

    val survivalRate = if (totalCount > 0) {
        (survivedCount.toFloat() / totalCount.toFloat() * 100).toInt()
    } else 0

    var animatedRate by remember { mutableFloatStateOf(0f) }
    val animatedRateState by animateFloatAsState(
        targetValue = survivalRate.toFloat(),
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "rate"
    )

    LaunchedEffect(survivalRate) {
        animatedRate = survivalRate.toFloat()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            GreenTertiary,
            GreenPrimary,
            GreenSecondary,
            Color(0xFFA5D6A7)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🌿",
                fontSize = 120.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 8.dp)
                    .alpha(0.15f)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🌿", fontSize = 56.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Namma Hasiru",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Our Green Mission 🌍",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🌱 Survival Score",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = EarthBrown
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .scale(pulseAnim)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        GreenSecondary,
                                        GreenPrimary,
                                        GreenTertiary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${animatedRateState.toInt()}%",
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Alive",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE8F5E9)
                        )
                    ) {
                        Text(
                            text = when {
                                survivalRate >= 80 -> "🎉 Excellent! Your green mission is thriving!"
                                survivalRate >= 60 -> "👍 Good work! Keep nurturing your plants!"
                                survivalRate >= 40 -> "💪 Keep going! Every plant matters!"
                                totalCount == 0 -> "🌱 Start planting to see your score!"
                                else -> "🤗 Don't give up! Nature needs you!"
                            },
                            fontSize = 13.sp,
                            color = EarthBrown,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard(
                    title = "Total",
                    value = totalCount.toString(),
                    emoji = "🌱",
                    color = Color(0xFFE8F5E9),
                    valueColor = GreenPrimary
                )
                StatCard(
                    title = "Sprouted",
                    value = survivedCount.toString(),
                    emoji = "✅",
                    color = Color(0xFFE8F5E9),
                    valueColor = Color(0xFF4CAF50)
                )
                StatCard(
                    title = "Died",
                    value = (totalCount - survivedCount).toString(),
                    emoji = "❌",
                    color = Color(0xFFFFEBEE),
                    valueColor = Color(0xFFE53935)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF8E1)
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🌍", fontSize = 40.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Did you know?",
                            fontWeight = FontWeight.Bold,
                            color = EarthBrown,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "One tree absorbs up to 22kg of CO₂ per year. Every plant you grow makes India greener! 🇮🇳",
                            fontSize = 12.sp,
                            color = EarthBrown.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎯 Mission Progress",
                        fontWeight = FontWeight.Bold,
                        color = EarthBrown,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MissionItem(
                            emoji = "🌱",
                            label = "Planted",
                            value = totalCount.toString()
                        )
                        MissionItem(
                            emoji = "🌳",
                            label = "Survived",
                            value = survivedCount.toString()
                        )
                        MissionItem(
                            emoji = "💚",
                            label = "CO₂ Saved",
                            value = "${survivedCount * 22}kg"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MissionItem(emoji: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 28.sp)
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = GreenPrimary
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = EarthBrown.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    emoji: String,
    color: Color,
    valueColor: Color
) {
    Card(
        modifier = Modifier.width(100.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = valueColor
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = EarthBrown
            )
        }
    }
}