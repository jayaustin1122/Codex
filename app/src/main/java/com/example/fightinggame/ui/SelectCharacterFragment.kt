package com.example.fightinggame.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.fightinggame.R
import com.example.fightinggame.dao.CharacterSelectionDao
import com.example.fightinggame.databinding.FragmentSelectCharacterBinding
import com.example.fightinggame.db.CodexDatabase
import com.example.fightinggame.model.CharacterSelection
import com.example.fightinggame.util.CharacterViewModelFactory
import com.example.fightinggame.viewmodels.CharacterViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SelectCharacterFragment : DialogFragment() {
    private lateinit var binding: FragmentSelectCharacterBinding
    private var selectedCharacter: com.example.fightinggame.model.Character? = null
    private var previouslySelectedView: ImageView? = null

    // Declare ViewModel
    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var characterSelectionDao: CharacterSelectionDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = FragmentSelectCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Initialize database and CharacterDao
        val characterDao = CodexDatabase.invoke(requireContext()).getCharacterDao()
        characterSelectionDao = CodexDatabase.invoke(requireContext()).getCharacterSelectionDao()

        // Initialize ViewModel with factory
        val factory = CharacterViewModelFactory(characterDao)
        characterViewModel = ViewModelProvider(this, factory).get(CharacterViewModel::class.java)

        loadCharactersFromDatabase()
        setupCharacterSelection()
    }

    private fun loadCharactersFromDatabase() {
        // Retrieve all characters from the database and bind to views
        characterViewModel.getAllCharacters { characters ->
            bindCharactersToViews(characters)
        }
    }

    private fun bindCharactersToViews(characters: List<com.example.fightinggame.model.Character>) {
        val characterViews = listOf(
            binding.character1,
            binding.character2,
            binding.character3,
            binding.character4
        )
        val characterNames = listOf(
            binding.characterName1,
            binding.characterName2,
            binding.characterName3,
            binding.characterName4
        )
        for ((index, character) in characters.withIndex()) {
            if (index < characterViews.size) {
                // Load character image (GIF)
                Glide.with(this)
                    .asGif()
                    .load(character.gifStand)
                    .into(characterViews[index])
                characterNames[index].text = character.name
                // Set the click listeners to highlight character
                characterViews[index].setOnClickListener {
                    highlightCharacter(characterViews[index], character)
                }
            }
        }
    }

    private fun setupCharacterSelection() {
        binding.selectButton.setOnClickListener {
            selectedCharacter?.let { character ->
                insertSelectedCharacter(character)
            }
            findNavController().navigate(R.id.mapsFragment)
            dismiss()
        }
    }

    private fun highlightCharacter(
        selected: ImageView,
        character: com.example.fightinggame.model.Character
    ) {
        previouslySelectedView?.setBackgroundResource(0)
        selected.setBackgroundResource(R.drawable.border_highlight)
        previouslySelectedView = selected
        selectedCharacter = character
        binding.selectButton.isEnabled = true

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun insertSelectedCharacter(character: com.example.fightinggame.model.Character) {
        GlobalScope.launch(Dispatchers.IO) {
            characterSelectionDao.deleteAllCharacterSelections()
        }
        val characterSelection = CharacterSelection(
            id = 1,
            gifStand = character.gifStand,
            gifAttack = character.gifAttack,

            )
        Toast.makeText(requireContext(), "You Select ${character.name}", Toast.LENGTH_SHORT).show();

        lifecycleScope.launch {
            characterSelectionDao.insertCharacterSelection(characterSelection)
        }
        Toast.makeText(requireContext(), "Overwriting progress...", Toast.LENGTH_SHORT).show()
    }
}
