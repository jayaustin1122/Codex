package com.example.fightinggame.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fightinggame.model.MonsterEnemy

@Dao
interface MonsterEnemyDao {

    // Insert a MonsterEnemy into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonsterEnemy(vararg monsterEnemy: MonsterEnemy)

    // Retrieve all MonsterEnemy entries
    @Query("SELECT * FROM monster_enemy_table")
    suspend fun getAllMonsters(): List<MonsterEnemy>

    // Retrieve a specific MonsterEnemy based on ID
    @Query("SELECT * FROM monster_enemy_table WHERE id = :monsterId LIMIT 1")
    suspend fun getMonsterById(monsterId: Int): MonsterEnemy?
}

