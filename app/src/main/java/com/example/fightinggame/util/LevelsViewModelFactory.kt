package com.example.fightinggame.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fightinggame.dao.CharacterDao
import com.example.fightinggame.dao.LevelsDao
import com.example.fightinggame.viewmodels.CharacterViewModel
import com.example.fightinggame.viewmodels.LevelsViewModel

class LevelsViewModelFactory(private val levelsDao: LevelsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LevelsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LevelsViewModel(levelsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
