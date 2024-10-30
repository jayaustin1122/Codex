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

    // Retrieve a character selection by a specific condition, e.g., gifStand or gifAttack
    @Query("SELECT * FROM character_selection WHERE gifStand = :standGif OR gifAttack = :attackGif")
    suspend fun getCharacterSelectionByGif(standGif: String?, attackGif: String?): CharacterSelection?
    // Delete all character selections
    @Query("DELETE FROM character_selection")
    suspend fun deleteAllCharacterSelections()
}
