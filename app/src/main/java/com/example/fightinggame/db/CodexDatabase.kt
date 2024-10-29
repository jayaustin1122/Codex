package com.example.fightinggame.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fightinggame.dao.CharacterDao
import com.example.fightinggame.dao.CharacterSelectionDao
import com.example.fightinggame.dao.LevelsDao
import com.example.fightinggame.dao.MonsterEnemyDao
import com.example.fightinggame.dao.TriviaDao
import com.example.fightinggame.dao.UserPointsDao
import com.example.fightinggame.model.Character
import com.example.fightinggame.model.CharacterSelection
import com.example.fightinggame.model.MonsterEnemy
import com.example.fightinggame.model.Trivia
import com.example.fightinggame.model.UserPoints
import com.example.fightinggame.model.mapsLevel

@Database(
    entities = [Character::class, CharacterSelection::class, mapsLevel::class, MonsterEnemy::class, Trivia::class, UserPoints::class],
    version = 1
)
abstract class CodexDatabase : RoomDatabase() {
    abstract fun getCharacterDao(): CharacterDao
    abstract fun getCharacterSelectionDao(): CharacterSelectionDao
    abstract fun getMapsLevelDao(): LevelsDao
    abstract fun getTriviaDao(): TriviaDao
    abstract fun getUserPointsDao(): UserPointsDao
    abstract fun getMonsterEnemyDao(): MonsterEnemyDao

    companion object {
        @Volatile
        private var instance: CodexDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, CodexDatabase::class.java, "codex.db"
        ).fallbackToDestructiveMigration() // This will allow for destructive migrations
            .build()


    }
}