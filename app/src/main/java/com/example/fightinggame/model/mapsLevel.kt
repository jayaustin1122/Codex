package com.example.fightinggame.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mapsLevel")
data class mapsLevel(
    val status: Boolean,  // Could be "completed", "in progress", etc.

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
