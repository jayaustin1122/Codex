package com.example.fightinggame.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fightinggame.model.Trivia

@Dao
interface TriviaDao {

    // Insert new trivia question
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrivia(triviaList: List<Trivia>)
    // Get all trivia by category
    @Query("SELECT * FROM trivia WHERE category = :category")
    suspend fun getTriviaByCategory(category: String): List<Trivia>

    // Get trivia that has not been asked yet (isAsked = false) in the given category
    @Query("SELECT * FROM trivia WHERE category = :category AND isAsked = 0")
    suspend fun getNewTriviaByCategory(category: String): List<Trivia>

    // Update trivia to mark it as asked (isAsked = true)
    @Update
    suspend fun updateTriviaStatus(trivia: Trivia)

    // Optionally: Get trivia that has already been asked (isAsked = true)
    @Query("SELECT * FROM trivia WHERE isAsked = 1")
    suspend fun getAskedTrivia(): List<Trivia>
}
