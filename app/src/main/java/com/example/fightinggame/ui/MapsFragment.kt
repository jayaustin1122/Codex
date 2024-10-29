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
import com.bumptech.glide.Glide
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
        playGif()
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
                    Question(
                        questionText = "What is the correct value to return to the operating system upon the successful completion of a program?",
                        choices = listOf("0", "1", "2", "-1"),
                        correctAnswerIndex = 0
                    ),
                    Question(
                        questionText = "What symbol is used to state the beginning and the end of blocks of code?",
                        choices = listOf("{ and }", "- and -", "( and )", "< and >"),
                        correctAnswerIndex = 0
                    ),
                    Question(
                        questionText = "Which of the following answers is the correct operator to compare two variables?",
                        choices = listOf("==", "&", "=", "&&"),
                        correctAnswerIndex = 0
                    ),
                    Question(
                        questionText = "Which of the following shows the correct syntax for an if statement?",
                        choices = listOf("if (expression)", "if {expression}", "if <expression>", "if expression"),
                        correctAnswerIndex = 0
                    )
                )
            )

            sharedViewModel.setCurrentLevel(level)

            findNavController().navigate(R.id.battleFragment)
        }
    }
    private fun playGif() {
        val torchViews = listOf(binding.level1Marker)
        torchViews.forEach { torchView ->
            Glide.with(this)
                .asGif()
                .load(R.drawable.torch)
                .into(torchView)
        }
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
