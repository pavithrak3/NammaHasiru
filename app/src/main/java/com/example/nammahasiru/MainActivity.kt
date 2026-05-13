package com.example.nammahasiru

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nammahasiru.ui.AddPlantScreen
import com.example.nammahasiru.ui.DashboardScreen
import com.example.nammahasiru.ui.MapScreen
import com.example.nammahasiru.ui.PlantListScreen
import com.example.nammahasiru.ui.SpeciesGuideScreen
import com.example.nammahasiru.ui.SplashScreen
import com.example.nammahasiru.ui.UpdateStatusScreen
import com.example.nammahasiru.ui.theme.NammaHasiruTheme
import com.example.nammahasiru.viewmodel.PlantViewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            NammaHasiruTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen(onSplashFinished = { showSplash = false })
                } else {
                    val navController = rememberNavController()
                    val viewModel: PlantViewModel = viewModel()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        bottomBar = {
                            if (currentRoute == "plant_list" ||
                                currentRoute == "dashboard" ||
                                currentRoute == "species_guide" ||
                                currentRoute == "map"
                            ) {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ) {
                                    NavigationBarItem(
                                        icon = {
                                            Icon(
                                                Icons.Default.Home,
                                                contentDescription = "Dashboard",
                                                tint = if (currentRoute == "dashboard")
                                                    MaterialTheme.colorScheme.primaryContainer
                                                else
                                                    MaterialTheme.colorScheme.onPrimary
                                            )
                                        },
                                        label = {
                                            Text(
                                                "Dashboard",
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        },
                                        selected = currentRoute == "dashboard",
                                        onClick = { navController.navigate("dashboard") },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                        )
                                    )
                                    NavigationBarItem(
                                        icon = {
                                            Icon(
                                                Icons.Default.List,
                                                contentDescription = "Plants",
                                                tint = if (currentRoute == "plant_list")
                                                    MaterialTheme.colorScheme.primaryContainer
                                                else
                                                    MaterialTheme.colorScheme.onPrimary
                                            )
                                        },
                                        label = {
                                            Text(
                                                "Plants",
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        },
                                        selected = currentRoute == "plant_list",
                                        onClick = { navController.navigate("plant_list") },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                        )
                                    )
                                    NavigationBarItem(
                                        icon = {
                                            Icon(
                                                Icons.Default.LocationOn,
                                                contentDescription = "Map",
                                                tint = if (currentRoute == "map")
                                                    MaterialTheme.colorScheme.primaryContainer
                                                else
                                                    MaterialTheme.colorScheme.onPrimary
                                            )
                                        },
                                        label = {
                                            Text(
                                                "Map",
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        },
                                        selected = currentRoute == "map",
                                        onClick = { navController.navigate("map") },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                        )
                                    )
                                    NavigationBarItem(
                                        icon = {
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = "Species Guide",
                                                tint = if (currentRoute == "species_guide")
                                                    MaterialTheme.colorScheme.primaryContainer
                                                else
                                                    MaterialTheme.colorScheme.onPrimary
                                            )
                                        },
                                        label = {
                                            Text(
                                                "Guide",
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        },
                                        selected = currentRoute == "species_guide",
                                        onClick = { navController.navigate("species_guide") },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                        )
                                    )
                                }
                            }
                        },
                        floatingActionButton = {
                            if (currentRoute == "plant_list") {
                                FloatingActionButton(
                                    onClick = { navController.navigate("add_plant") },
                                    containerColor = MaterialTheme.colorScheme.primary
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Add Plant",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    ) { padding ->
                        NavHost(
                            navController = navController,
                            startDestination = "dashboard",
                            modifier = androidx.compose.ui.Modifier.padding(padding)
                        ) {
                            composable("dashboard") {
                                DashboardScreen(viewModel = viewModel)
                            }
                            composable("plant_list") {
                                PlantListScreen(
                                    viewModel = viewModel,
                                    onAddPlant = { navController.navigate("add_plant") },
                                    onPlantClick = { plant ->
                                        navController.navigate("update_status/${plant.id}")
                                    }
                                )
                            }
                            composable("add_plant") {
                                AddPlantScreen(
                                    viewModel = viewModel,
                                    onPlantSaved = { navController.popBackStack() }
                                )
                            }
                            composable("update_status/{plantId}") { backStackEntry ->
                                val plantId =
                                    backStackEntry.arguments?.getString("plantId")?.toIntOrNull()
                                if (plantId != null) {
                                    val plants by viewModel.allPlants.collectAsState(initial = emptyList())
                                    val selectedPlant = plants.find { it.id == plantId }
                                    if (selectedPlant != null) {
                                        UpdateStatusScreen(
                                            plant = selectedPlant,
                                            viewModel = viewModel,
                                            onStatusUpdated = { navController.popBackStack() }
                                        )
                                    }
                                }
                            }
                            composable("map") {
                                MapScreen(viewModel = viewModel)
                            }
                            composable("species_guide") {
                                SpeciesGuideScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}