package com.example.fightinggame.viewmodels

import com.example.fightinggame.dao.CharacterDao
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fightinggame.model.Character
import kotlinx.coroutines.launch

class CharacterViewModel(private val characterDao: CharacterDao) : ViewModel() {

    fun getAllCharacters(callback: (List<Character>) -> Unit) {
        viewModelScope.launch {
            val characters = characterDao.getAllCharacters() // Fetching from DAO
            callback(characters)
        }
    }
}
