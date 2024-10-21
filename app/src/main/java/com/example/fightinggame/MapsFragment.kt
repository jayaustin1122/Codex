package com.example.fightinggame

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fightinggame.databinding.FragmentMapsBinding

class MapsFragment : Fragment() {
    private lateinit var binding: FragmentMapsBinding
    private val originalGif = R.drawable.hero1
    private val attackGif = R.drawable.hero1_attack

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedCharacterId = arguments?.getInt("SELECTED_CHARACTER_ID", -1)
        val selectedCharacterAttackId = arguments?.getInt("SELECTED_CHARACTER_ATTACK_ID", -1)


        showDungeonAdventureDialog(requireContext())

        binding.level1Marker.setOnClickListener {
            val bundle = Bundle().apply {
                if (selectedCharacterId != null) {
                    putInt("SELECTED_CHARACTER_ID", selectedCharacterId)
                }
                if (selectedCharacterAttackId != null) {
                    putInt("SELECTED_CHARACTER_ATTACK_ID", selectedCharacterAttackId)
                }
            }
            findNavController().navigate(R.id.battleFragment, bundle)
        }
    }

    private fun showDungeonAdventureDialog(context: Context) {
        val message = """
            Storyline: You are a young scholar from the ancient kingdom of Arcanum, a land where knowledge is as powerful as magic. The kingdom has long been guarded by a mystical artifact known as the Codex of Wisdom, which ensures peace and prosperity. However, the Codex has been shattered into fragments by an evil sorcerer, Malakar, who seeks to plunge the world into chaos by erasing all knowledge and learning.


To restore the Codex, you are chosen by the High Council of Sages to embark on a grand quest: The Trials of Arcanum. In these trials, you must travel across the vast lands of Arcanum—through enchanted forests, ancient ruins, and bustling cities—facing powerful creatures and solving intellectual challenges.


Each fragment of the Codex is guarded by a Quizmaster, a mystical being who will test your wisdom in programming language. The more questions you answer correctly, the stronger you become. Wrong answers, however, can lead to traps, monsters, or losing precious health.


Your ultimate goal is to defeat all monster in each level and confront Malakar in his fortress of shadows. But beware, for every quiz challenge you overcome, Malakar grows stronger too, learning your weaknesses and trying to outsmart you in the final confrontation.""".trimIndent()

        AlertDialog.Builder(context)
            .setTitle("Dungeon of Code Adventure")
            .setMessage(message)
            .setPositiveButton("Continue") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Exit") { dialog, _ -> dialog.dismiss() }
            .show()
    }




}
