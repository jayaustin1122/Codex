package com.example.fightinggame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.fightinggame.databinding.FragmentBattleBinding

class BattleFragment : Fragment() {
    private lateinit var binding: FragmentBattleBinding

    // Placeholders for character ID and attack animation
    private var selectedCharacterId: Int? = null
    private var selectedCharacterAttackId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBattleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve passed character ID and attack ID from the arguments
        selectedCharacterId = arguments?.getInt("SELECTED_CHARACTER_ID", -1)
        selectedCharacterAttackId = arguments?.getInt("SELECTED_CHARACTER_ATTACK_ID", -1)

        // Check and display the selected character standing GIF
        selectedCharacterId?.let { id ->
            if (id != -1) {
                displaySelectedCharacterGif(id)
            }
        }


        binding.timerText.text = "99"
        binding.player1Character.setOnClickListener {
            performAttack() // Perform attack animation on character click
        }
    }

    private fun performAttack() {

        selectedCharacterAttackId?.let { attackId ->
            if (attackId != -1) {
                Glide.with(this)
                    .asGif()
                    .load(attackId)
                    .into(binding.player1Character)

                // Revert back to the original
                Handler(Looper.getMainLooper()).postDelayed({
                    selectedCharacterId?.let { id ->
                        playGif(id)
                    }
                }, 2000)
            }
        }
    }

    private fun displaySelectedCharacterGif(characterId: Int) {
        // Load the selected character standing GIF into the player avatar
        Glide.with(this)
            .asGif()
            .load(characterId)
            .into(binding.player1Character)
    }

    private fun playGif(characterId: Int) {
        // Reset to the original standing GIF after the attack animation
        Glide.with(this)
            .asGif()
            .load(characterId)
            .into(binding.player1Character)
    }
}
