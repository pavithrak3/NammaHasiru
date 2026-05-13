package com.example.nammahasiru.data

import kotlinx.coroutines.flow.Flow

class PlantRepository(private val plantDao: PlantDao) {
    val allPlants: Flow<List<Plant>> = plantDao.getAllPlants()
    val survivedCount: Flow<Int> = plantDao.getSurvivedCount()
    val totalCount: Flow<Int> = plantDao.getTotalCount()

    suspend fun insert(plant: Plant) = plantDao.insertPlant(plant)
    suspend fun update(plant: Plant) = plantDao.updatePlant(plant)
    suspend fun delete(plant: Plant) = plantDao.deletePlant(plant)
}