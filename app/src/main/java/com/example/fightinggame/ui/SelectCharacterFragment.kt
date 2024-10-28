package com.example.fightinggame.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fightinggame.R
import com.example.fightinggame.databinding.FragmentSelectCharacterBinding

class SelectCharacterFragment : DialogFragment() {
    private lateinit var binding: FragmentSelectCharacterBinding
    private var selectedCharacter: ImageView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCharacterGifs()
        setupCharacterSelection()
    }

    private fun loadCharacterGifs() {
        Glide.with(this)
            .asGif()
            .load(R.drawable.hero1)
            .into(binding.character1)

        Glide.with(this)
            .asGif()
            .load(R.drawable.hero2)
            .into(binding.character2)

        Glide.with(this)
            .asGif()
            .load(R.drawable.hero3)
            .into(binding.character3)

        Glide.with(this)
            .asGif()
            .load(R.drawable.hero4)
            .into(binding.character4)
    }

    private fun setupCharacterSelection() {
        val characters = listOf(
            binding.character1,
            binding.character2,
            binding.character3,
            binding.character4
        )

        for (character in characters) {
            character.setOnClickListener {
                highlightCharacter(character)
            }
        }

        binding.selectButton.setOnClickListener {

            selectedCharacter?.let { character ->
                val (characterId, characterAttackId) = when (character.id) {
                    R.id.character1 -> R.drawable.hero1 to R.drawable.hero1_attack
                    R.id.character2 -> R.drawable.hero2 to R.drawable.hero1_attack
                    R.id.character3 -> R.drawable.hero3 to R.drawable.hero1_attack
                    R.id.character4 -> R.drawable.hero4 to R.drawable.hero1_attack
                    else -> -1 to -1
                }

                if (characterId != -1 && characterAttackId != -1) {
                    val bundle = Bundle().apply {
                        putInt("SELECTED_CHARACTER_ID", characterId)
                        putInt("SELECTED_CHARACTER_ATTACK_ID", characterAttackId)
                    }
                    findNavController().navigate(R.id.mapsFragment, bundle)
                }
            }
            dismiss()
        }

    }

    private fun highlightCharacter(selected: ImageView) {
        // Reset previously selected character border
        selectedCharacter?.setBackgroundResource(R.drawable.border_selector)
        selectedCharacter = selected
        selected.setBackgroundResource(R.drawable.border_highlight)
        binding.selectButton.isEnabled = true
    }
}
