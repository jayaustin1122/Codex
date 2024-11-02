package com.example.fightinggame.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fightinggame.model.TriviaQuestionUserAnswer
@Dao
interface TriviaUserAnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTriviaQuestion(triviaQuestion: TriviaQuestionUserAnswer)

    @Query("SELECT * FROM trivia_questions_answers WHERE level = :level ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomTriviaByLevel(level: Int): TriviaQuestionUserAnswer?

    @Query("SELECT * FROM trivia_questions_answers WHERE level = :level")
    suspend fun getTriviaQuestionsById(level: Int): List<TriviaQuestionUserAnswer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleTriviaQuestions(trivias: List<TriviaQuestionUserAnswer>)
    // New method to delete all trivia questions
    @Query("DELETE FROM trivia_questions_answers")
    suspend fun deleteAllTriviaQuestions()
}
