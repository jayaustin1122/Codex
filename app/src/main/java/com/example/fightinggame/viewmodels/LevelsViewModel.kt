package com.example.fightinggame.viewmodels

import com.example.fightinggame.dao.CharacterDao
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fightinggame.dao.LevelsDao
import com.example.fightinggame.model.Character
import com.example.fightinggame.model.mapsLevel
import kotlinx.coroutines.launch

class LevelsViewModel(private val levelsDao: LevelsDao) : ViewModel() {

    fun getAllCharacters(callback: (List<mapsLevel>) -> Unit) {
        viewModelScope.launch {
            val characters = levelsDao.getAllLevels() // Fetching from DAO
            callback(characters)
        }
    }
}
