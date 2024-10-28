package com.example.fightinggame.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.fightinggame.model.Level

class SharedViewModel(private val state: SavedStateHandle) : ViewModel() {

    companion object {
        private const val SELECTED_CHARACTER_ID = "SELECTED_CHARACTER_ID"
        private const val SELECTED_CHARACTER_ATTACK_ID = "SELECTED_CHARACTER_ATTACK_ID"
        private const val CURRENT_LEVEL = "CURRENT_LEVEL"
    }

    // Getters and Setters for selected character and attack ID
    fun setSelectedCharacterId(id: Int) {
        state[SELECTED_CHARACTER_ID] = id
    }

    fun getSelectedCharacterId(): Int? {
        return state[SELECTED_CHARACTER_ID]
    }

    fun setSelectedCharacterAttackId(id: Int) {
        state[SELECTED_CHARACTER_ATTACK_ID] = id
    }

    fun getSelectedCharacterAttackId(): Int? {
        return state[SELECTED_CHARACTER_ATTACK_ID]
    }

    // Save the current level
    fun setCurrentLevel(level: Level) {
        state[CURRENT_LEVEL] = level
    }

    fun getCurrentLevel(): Level? {
        return state[CURRENT_LEVEL]
    }
}
