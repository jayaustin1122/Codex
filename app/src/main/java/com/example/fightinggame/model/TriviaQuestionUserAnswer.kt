package com.example.fightinggame.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trivia_questions_answers")
class TriviaQuestionUserAnswer (
    val question: String,
    val correctAnswerIndex: String,
    val number: Int,
    val ans1: String,
    val ans2: String,
    val ans3: String,
    val ans4: String,
    val level: Int,
    val userSelectAnswer: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)