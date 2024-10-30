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

    @Query("SELECT * FROM trivia WHERE isAsked = 0 ORDER BY RANDOM() LIMIT 10")
    suspend fun getRandomNewTrivia(): List<Trivia>

    // Update trivia to mark it as asked (isAsked = true)
    @Update
    suspend fun updateTriviaStatus(trivia: Trivia)
    // Update trivia to mark it as asked by ID
    @Query("UPDATE trivia SET isAsked = 1 WHERE number = :triviaId")
    suspend fun markQuestionAsAskedById(triviaId: Int)
    // Optionally: Get trivia that has already been asked (isAsked = true)
    @Query("SELECT * FROM trivia WHERE isAsked = 1")
    suspend fun getAskedTrivia(): List<Trivia>
}
