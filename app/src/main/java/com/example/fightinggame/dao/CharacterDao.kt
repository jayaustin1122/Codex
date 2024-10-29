package com.example.fightinggame.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(vararg characters: com.example.fightinggame.model.Character)

    // Retrieve all Character entries
    @Query("SELECT * FROM character_table")
    suspend fun getAllCharacters(): List<com.example.fightinggame.model.Character>

    // Retrieve a specific Character based on ID
    @Query("SELECT * FROM character_table WHERE id = :characterId LIMIT 1")
    suspend fun getCharacterById(characterId: Int): com.example.fightinggame.model.Character?
}
