package com.example.nammahasiru.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.nammahasiru.data.Plant
import com.example.nammahasiru.ui.theme.EarthBrown
import com.example.nammahasiru.ui.theme.GreenPrimary
import com.example.nammahasiru.ui.theme.GreenSecondary
import com.example.nammahasiru.ui.theme.GreenTertiary
import com.example.nammahasiru.viewmodel.PlantViewModel

@Composable
fun PlantListScreen(
    viewModel: PlantViewModel,
    onAddPlant: () -> Unit,
    onPlantClick: (Plant) -> Unit
) {
    val plants by viewModel.allPlants.collectAsState(initial = emptyList())
    var plantToDelete by remember { mutableStateOf<Plant?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredPlants = if (searchQuery.isBlank()) {
        plants
    } else {
        plants.filter {
            it.speciesName.contains(searchQuery, ignoreCase = true)
        }
    }

    if (plantToDelete != null) {
        AlertDialog(
            onDismissRequest = { plantToDelete = null },
            title = {
                Text(
                    text = "🗑️ Delete Plant",
                    fontWeight = FontWeight.Bold,
                    color = EarthBrown
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete ${plantToDelete!!.speciesName}? This cannot be undone.",
                    color = EarthBrown
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deletePlant(plantToDelete!!)
                        plantToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("🗑️ Delete", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { plantToDelete = null },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", color = Color.White)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F8E9)),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(GreenTertiary, GreenPrimary)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Text(
                        text = "🌿",
                        fontSize = 80.sp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .alpha(0.15f)
                    )
                    Column {
                        Text(
                            text = "🌿 My Plants",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${plants.size} plants • Long press to delete",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Search bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search plants...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = Color(0xFFB0BEC5),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = GreenPrimary
                        )
                    },
                    singleLine = true
                )
            }

            // Empty state
            if (filteredPlants.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🌱", fontSize = 72.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (searchQuery.isBlank()) "No plants yet!" else "No plants found!",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = EarthBrown
                            )
                            Text(
                                text = if (searchQuery.isBlank())
                                    "Tap + to start your green journey 🌿"
                                else
                                    "Try a different search term",
                                fontSize = 14.sp,
                                color = EarthBrown.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            } else {
                // Plant cards
                itemsIndexed(filteredPlants) { _, plant ->
                    Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                        PlantCard(
                            plant = plant,
                            onClick = { onPlantClick(plant) },
                            onLongPress = { plantToDelete = plant }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddPlant,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = GreenPrimary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Plant",
                tint = Color.White
            )
        }
    }
}

@Composable
fun PlantCard(plant: Plant, onClick: () -> Unit, onLongPress: () -> Unit) {
    val statusColor = when (plant.status) {
        "Sprouted" -> Color(0xFF4CAF50)
        "Died" -> Color(0xFFE53935)
        else -> Color(0xFFFF9800)
    }

    val statusEmoji = when (plant.status) {
        "Sprouted" -> "✅"
        "Died" -> "❌"
        else -> "❓"
    }

    val cardColor = when (plant.status) {
        "Sprouted" -> Color(0xFFF1F8E9)
        "Died" -> Color(0xFFFFEBEE)
        else -> Color(0xFFFFFDE7)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongPress() }
                )
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (plant.photoPath != null) {
                Image(
                    painter = rememberAsyncImagePainter(plant.photoPath),
                    contentDescription = "Plant Photo",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    statusColor.copy(alpha = 0.3f),
                                    statusColor.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌱", fontSize = 36.sp)
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plant.speciesName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = EarthBrown
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(statusColor.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$statusEmoji ${plant.status}",
                        fontSize = 12.sp,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (plant.locationAddress != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📍 ${plant.locationAddress}",
                        fontSize = 11.sp,
                        color = GreenSecondary,
                        fontWeight = FontWeight.Medium
                    )
                } else if (plant.latitude != 0.0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📍 Location captured",
                        fontSize = 11.sp,
                        color = GreenSecondary
                    )
                }
            }
        }
    }
}