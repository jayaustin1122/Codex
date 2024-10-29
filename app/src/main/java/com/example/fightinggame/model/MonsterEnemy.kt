package com.example.fightinggame.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "monster_enemy_table")
data class MonsterEnemy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val levelAssigned: Int,
    val gifStand: Int?, // Path to the standing GIF
    val gifAttack: Int? // Path to the attacking GIF
)

