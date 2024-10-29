package com.example.fightinggame.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_table")
data class Character(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val level: Int,
    val gifStand: Int?,   // Path to the standing GIF
    val gifAttack: Int?   // Path to the attacking GIF
)
