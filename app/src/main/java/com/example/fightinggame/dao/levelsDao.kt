package com.example.fightinggame.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fightinggame.model.mapsLevel

@Dao
interface LevelsDao {

    // Insert new level data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevels(vararg level: mapsLevel)

    // Retrieve all levels with their status
    @Query("SELECT * FROM mapsLevel")
    suspend fun getAllLevels(): List<mapsLevel>

    // Retrieve a specific level by its ID
    @Query("SELECT * FROM mapsLevel WHERE id = :levelId LIMIT 1")
    suspend fun getLevelById(levelId: Long): mapsLevel?

    // Update level status (e.g., completed, in progress)
    @Update
    suspend fun updateLevel(level: mapsLevel)

    // Optionally: Get only completed levels
    @Query("SELECT * FROM mapsLevel WHERE status = :status")
    suspend fun getLevelsByStatus(status: String): List<mapsLevel>
}
