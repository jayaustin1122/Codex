package com.example.fightinggame.model

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "trivia")
data class Trivia(
    val question: String,
    val category: String,
    val difficulty: String,
    val ans1: String,
    val ans2: String,
    val ans3: String,
    val ans4: String,
    val correctAnswerIndex: String,
    val number: Int,
    var isAsked: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
