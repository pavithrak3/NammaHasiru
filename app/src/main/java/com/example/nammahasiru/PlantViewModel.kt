package com.example.nammahasiru.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.nammahasiru.PlantReminderWorker
import com.example.nammahasiru.data.Plant
import com.example.nammahasiru.data.PlantDatabase
import com.example.nammahasiru.data.PlantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlantRepository
    val allPlants: Flow<List<Plant>>
    val survivedCount: Flow<Int>
    val totalCount: Flow<Int>

    init {
        val dao = PlantDatabase.getDatabase(application).plantDao()
        repository = PlantRepository(dao)
        allPlants = repository.allPlants
        survivedCount = repository.survivedCount
        totalCount = repository.totalCount
    }

    fun insertPlant(plant: Plant) = viewModelScope.launch {
        repository.insert(plant)
        scheduleReminder(plant.speciesName)
    }

    fun updatePlant(plant: Plant) = viewModelScope.launch {
        repository.update(plant)
    }

    fun deletePlant(plant: Plant) = viewModelScope.launch {
        repository.delete(plant)
    }

    private fun scheduleReminder(plantName: String) {
        val data = Data.Builder()
            .putString("plant_name", plantName)
            .build()

        // 10 second reminder - just to confirm it's working
        val quickReminderRequest = OneTimeWorkRequestBuilder<PlantReminderWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(data)
            .build()

        // 90 day reminder - actual check reminder
        val fullReminderRequest = OneTimeWorkRequestBuilder<PlantReminderWorker>()
            .setInitialDelay(90, TimeUnit.DAYS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(getApplication()).enqueue(quickReminderRequest)
        WorkManager.getInstance(getApplication()).enqueue(fullReminderRequest)
    }
}