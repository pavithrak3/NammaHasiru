package com.example.nammahasiru.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: Plant)

    @Query("SELECT * FROM plants")
    fun getAllPlants(): Flow<List<Plant>>

    @Query("SELECT * FROM plants WHERE id = :id")
    suspend fun getPlantById(id: Int): Plant?

    @Update
    suspend fun updatePlant(plant: Plant)

    @Delete
    suspend fun deletePlant(plant: Plant)

    @Query("SELECT COUNT(*) FROM plants WHERE status = 'Sprouted'")
    fun getSurvivedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM plants")
    fun getTotalCount(): Flow<Int>
}