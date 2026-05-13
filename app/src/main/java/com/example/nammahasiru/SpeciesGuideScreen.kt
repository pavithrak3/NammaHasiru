package com.example.nammahasiru.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nammahasiru.data.Plant
import com.example.nammahasiru.ui.theme.EarthBrown
import com.example.nammahasiru.ui.theme.GreenPrimary
import com.example.nammahasiru.ui.theme.GreenSecondary
import com.example.nammahasiru.ui.theme.GreenTertiary
import com.example.nammahasiru.viewmodel.PlantViewModel

@Composable
fun SpeciesGuideScreen(viewModel: PlantViewModel) {
    val plants by viewModel.allPlants.collectAsState(initial = emptyList())

    // Group plants by species and calculate survival rate
    val speciesStats = plants
        .groupBy { it.speciesName.lowercase().trim() }
        .map { (species, plantList) ->
            val total = plantList.size
            val sprouted = plantList.count { it.status == "Sprouted" }
            val survivalRate = if (total > 0) (sprouted * 100 / total) else 0
            SpeciesStat(
                name = plantList.first().speciesName,
                total = total,
                sprouted = sprouted,
                survivalRate = survivalRate
            )
        }
        .sortedByDescending { it.survivalRate }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GreenTertiary, GreenPrimary)
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🌿", fontSize = 40.sp)
                Text(
                    text = "Species Guide",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Best plants based on your data",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        if (speciesStats.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🌱", fontSize = 64.sp)
                    Text(
                        text = "No data yet!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = EarthBrown
                    )
                    Text(
                        text = "Add plants and update their status\nto see species insights",
                        fontSize = 14.sp,
                        color = EarthBrown.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(12.dp)) {

                // Top performer card
                item {
                    if (speciesStats.isNotEmpty() && speciesStats.first().survivalRate > 0) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F5E9)
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🏆", fontSize = 32.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Best Performer!",
                                        fontWeight = FontWeight.Bold,
                                        color = GreenPrimary,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = "${speciesStats.first().name} has the highest survival rate of ${speciesStats.first().survivalRate}%",
                                        fontSize = 13.sp,
                                        color = EarthBrown
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "All Species",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = EarthBrown,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                itemsIndexed(speciesStats) { index, stat ->
                    SpeciesCard(stat = stat, rank = index + 1)
                }
            }
        }
    }
}

data class SpeciesStat(
    val name: String,
    val total: Int,
    val sprouted: Int,
    val survivalRate: Int
)

@Composable
fun SpeciesCard(stat: SpeciesStat, rank: Int) {
    val barColor = when {
        stat.survivalRate >= 70 -> Color(0xFF4CAF50)
        stat.survivalRate >= 40 -> Color(0xFFFF9800)
        else -> Color(0xFFE53935)
    }

    val medal = when {
        stat.survivalRate == 100 -> "🥇"
        stat.survivalRate >= 70 -> "🥈"
        stat.survivalRate >= 40 -> "🥉"
        else -> "🌱"

    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(GreenSecondary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = medal, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stat.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = EarthBrown
                    )
                    Text(
                        text = "${stat.total} planted • ${stat.sprouted} sprouted",
                        fontSize = 12.sp,
                        color = EarthBrown.copy(alpha = 0.6f)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(barColor.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${stat.survivalRate}%",
                        fontWeight = FontWeight.Bold,
                        color = barColor,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFE0E0E0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(stat.survivalRate / 100f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(barColor)
                )
            }
        }
    }
}