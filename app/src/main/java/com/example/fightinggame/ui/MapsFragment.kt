package com.example.fightinggame.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fightinggame.R
import com.example.fightinggame.databinding.FragmentMapsBinding
import com.example.fightinggame.model.Level
import com.example.fightinggame.model.Question
import com.example.fightinggame.viewmodels.SharedViewModel

class MapsFragment : Fragment() {
    private lateinit var binding: FragmentMapsBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get selected character and attack ID
        val selectedCharacterId = arguments?.getInt("SELECTED_CHARACTER_ID", -1)
        val selectedCharacterAttackId = arguments?.getInt("SELECTED_CHARACTER_ATTACK_ID", -1)

        // Store selected character and attack in the ViewModel
        selectedCharacterId?.let {
            sharedViewModel.setSelectedCharacterId(it)
        }
        selectedCharacterAttackId?.let {
            sharedViewModel.setSelectedCharacterAttackId(it)
        }


        showDungeonAdventureDialog(requireContext())


        binding.level1Marker.setOnClickListener {
            val level = Level(
                levelNumber = 1,
                questions = listOf(
                    Question("What is Kotlin?", listOf("Language", "IDE"), 0),
                    Question("What is the latest Android version?", listOf("Pie", "Oreo"), 1)
                )
            )
            sharedViewModel.setCurrentLevel(level)

            findNavController().navigate(R.id.battleFragment)
        }
    }

    private fun showDungeonAdventureDialog(context: Context) {
        val message = """
            Storyline: You are a young scholar from the ancient kingdom...
        """.trimIndent()

        AlertDialog.Builder(context)
            .setTitle("Dungeon of Code Adventure")
            .setMessage(message)
            .setPositiveButton("Continue") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Exit") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
