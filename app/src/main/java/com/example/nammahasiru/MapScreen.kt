package com.example.nammahasiru.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.nammahasiru.R
import com.example.nammahasiru.ui.theme.GreenPrimary
import com.example.nammahasiru.ui.theme.GreenTertiary
import androidx.compose.ui.graphics.Brush
import com.example.nammahasiru.viewmodel.PlantViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(viewModel: PlantViewModel) {
    val plants by viewModel.allPlants.collectAsState(initial = emptyList())
    val context = LocalContext.current

    // Filter plants that have location
    val plantsWithLocation = plants.filter { it.latitude != 0.0 && it.longitude != 0.0 }

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
                Text(text = "🗺️", fontSize = 40.sp)
                Text(
                    text = "Plant Map",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${plantsWithLocation.size} plants on map",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        if (plantsWithLocation.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🗺️", fontSize = 64.sp)
                    Text(
                        text = "No plants on map yet!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add plants with location\nto see them on the map",
                        fontSize = 14.sp,
                        color = Color(0xFF5D4037).copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            AndroidView(
                factory = { ctx ->
                    Configuration.getInstance().userAgentValue = ctx.packageName
                    val mapView = MapView(ctx)
                    mapView.setTileSource(TileSourceFactory.MAPNIK)
                    mapView.setMultiTouchControls(true)

                    // Set initial position to first plant
                    val firstPlant = plantsWithLocation.first()
                    mapView.controller.setZoom(15.0)
                    mapView.controller.setCenter(
                        GeoPoint(firstPlant.latitude, firstPlant.longitude)
                    )

                    // Add markers for each plant
                    plantsWithLocation.forEach { plant ->
                        val marker = Marker(mapView)
                        marker.position = GeoPoint(plant.latitude, plant.longitude)
                        marker.title = plant.speciesName
                        marker.snippet = "Status: ${plant.status}"

                        // Set marker color based on status
                        val markerColor = when (plant.status) {
                            "Sprouted" -> android.graphics.Color.GREEN
                            "Died" -> android.graphics.Color.RED
                            else -> android.graphics.Color.YELLOW
                        }

                        marker.icon = getColoredMarker(ctx, markerColor)
                        mapView.overlays.add(marker)
                    }

                    mapView
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

fun getColoredMarker(context: Context, color: Int): Drawable? {
    val drawable = ContextCompat.getDrawable(
        context,
        android.R.drawable.ic_menu_mylocation
    )
    drawable?.setTint(color)
    return drawable
}