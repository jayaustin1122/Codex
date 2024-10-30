package com.example.fightinggame.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fightinggame.R
import com.example.fightinggame.dao.LevelsDao
import com.example.fightinggame.databinding.FragmentMapsBinding
import com.example.fightinggame.db.CodexDatabase
import com.example.fightinggame.model.mapsLevel
import com.example.fightinggame.util.LevelsViewModelFactory
import com.example.fightinggame.viewmodels.LevelsViewModel

class MapsFragment : Fragment() {
    private lateinit var binding: FragmentMapsBinding
    private lateinit var levelsDao: LevelsDao
    private lateinit var levelsViewModel: LevelsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        levelsDao = CodexDatabase.invoke(requireContext()).getMapsLevelDao()
        val factory = LevelsViewModelFactory(levelsDao)
        levelsViewModel = ViewModelProvider(this, factory).get(LevelsViewModel::class.java)

        showLevels()
       // showDungeonAdventureDialog(requireContext())
        binding.level1Marker.setOnClickListener {
            findNavController().navigate(R.id.battleFragment)
        }
    }

    private fun showLevels() {
        levelsViewModel.getAllCharacters { levels ->
            Log.d("ShowLevels", "Retrieved levels: $levels")
            bindCharactersToViews(levels)
        }
    }
    private fun bindCharactersToViews(levels: List<mapsLevel>) {
        // List of corresponding views for each level marker
        val levelMarkers = listOf(
            binding.level1Marker,
            binding.level2Marker,
            binding.level3Marker,
            binding.level4Marker,
            binding.level5Marker,
        )

        // Loop through each level and bind it to the corresponding marker
        for ((index, level) in levels.withIndex()) {
            if (index < levelMarkers.size) {
                val markerView = levelMarkers[index]

                if (level.status) {
                    // Play GIF for unlocked levels
                    playGif(markerView)
                } else {
                    // Show toast that level is locked
                    markerView.setOnClickListener {
                        Toast.makeText(
                            requireContext(),
                            "This level is locked. Please finish another level to unlock.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                // Set the navigation to the battle fragment if the level is unlocked
                markerView.setOnClickListener {
                    if (level.status) {
                        // Increment index by 1 before passing to the next fragment
                        val adjustedIndex = index + 1

                        // Pass the adjusted index to the next fragment using a Bundle
                        val bundle = Bundle().apply {
                            putInt("selected_level_index", adjustedIndex)  // Pass the adjusted index
                        }

                        findNavController().navigate(R.id.battleFragment, bundle)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "This level is locked. Please finish another level to unlock.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }
    }

    // Play GIF on unlocked levels
    private fun playGif(view: View) {
        Glide.with(this)
            .asGif()
            .load(R.drawable.torch)  // Assume torch is your gif resource
            .into(view as ImageView)
    }

    private fun showDungeonAdventureDialog(context: Context) {
        val message = """
        You are a young scholar from the ancient kingdom of Arcanum, a land where knowledge is as powerful as magic. The kingdom has long been guarded by a mystical artifact known as the Codex of Wisdom, which ensures peace and prosperity. However, the Codex has been shattered into fragments by an evil sorcerer, Malakar, who seeks to plunge the world into chaos by erasing all knowledge and learning.

        To restore the Codex, you are chosen by the High Council of Sages to embark on a grand quest: The Trials of Arcanum. In these trials, you must travel across the vast lands of Arcanum—through enchanted forests, ancient ruins, and bustling cities—facing powerful creatures and solving intellectual challenges.

        Each fragment of the Codex is guarded by a Quizmaster, a mystical being who will test your wisdom in programming language. The more questions you answer correctly, the stronger you become. Wrong answers, however, can lead to traps, monsters, or losing precious health.

        Your ultimate goal is to defeat all monsters in each level and confront Malakar in his fortress of shadows. But beware, for every quiz challenge you overcome, Malakar grows stronger too, learning your weaknesses and trying to outsmart you in the final confrontation.
    """.trimIndent()

        val dialogBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialogTheme)
            .setTitle("Dungeon of Code Adventure")
            .setMessage(message)
            .setPositiveButton("Continue") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Exit") { dialog, _ -> findNavController().navigateUp() }

        val dialog = dialogBuilder.create()

        // Apply custom background from the drawable resource
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        dialog.show()
    }

}
