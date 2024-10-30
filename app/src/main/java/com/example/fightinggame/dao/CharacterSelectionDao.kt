package com.example.fightinggame.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fightinggame.model.CharacterSelection

@Dao
interface CharacterSelectionDao {

    // Insert a new character selection entry into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterSelection(characterSelection: CharacterSelection)

    // Retrieve all character selections
    @Query("SELECT * FROM character_selection")
    suspend fun getAllCharacterSelections(): List<CharacterSelection>

    // Retrieve a character selection by ID
    @Query("SELECT * FROM character_selection WHERE id = :characterId")
    suspend fun getCharacterSelectionById(characterId: Int): CharacterSelection?

    // Delete all character selections
    @Query("DELETE FROM character_selection")
    suspend fun deleteAllCharacterSelections()
}
