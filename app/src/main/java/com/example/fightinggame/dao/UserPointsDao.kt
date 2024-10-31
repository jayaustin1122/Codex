package com.example.fightinggame.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fightinggame.model.User
import com.example.fightinggame.model.UserPoints

@Dao
interface UserPointsDao {

    // Insert a new user points entry into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPoints(userPoints: UserPoints)

    @Query("SELECT * FROM user_points WHERE id = :id LIMIT 1")
    suspend fun getUserPoints(id: Int): UserPoints?
    // Update existing user points in the database
    @Update
    suspend fun updateUserPoints(userPoints: UserPoints)
}
