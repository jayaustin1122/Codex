package com.example.fightinggame.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_selection")
data class CharacterSelection(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var gifStand: String? = null,
    var gifAttack: String? = null
)
