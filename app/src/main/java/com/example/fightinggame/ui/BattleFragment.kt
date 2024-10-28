package com.example.fightinggame.ui

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.fightinggame.R
import com.example.fightinggame.databinding.FragmentBattleBinding
import com.example.fightinggame.viewmodels.SharedViewModel

class BattleFragment : Fragment() {
    private lateinit var binding: FragmentBattleBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // Variables to track health
    private var monsterHealth = 100
    private var playerHealth = 100
    private var playerPoints = 0
    private lateinit var countDownTimer: CountDownTimer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBattleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTimer()
        // Retrieve selected character and attack ID from ViewModel
        val selectedCharacterId = sharedViewModel.getSelectedCharacterId()
        val selectedCharacterAttackId = sharedViewModel.getSelectedCharacterAttackId()

        // Display the selected character standing GIF
        selectedCharacterId?.let { id ->
            if (id != -1) {
                displaySelectedCharacterGif(id)
                displaySelectedCharacterGifEnemy(id)
            }
        }

        // Set initial health display
        updateHealthDisplay()

        // Attack action
        binding.player1Character.setOnClickListener {
            showQuestionDialog()
        }
    }
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(60000, 1000) { // 1 minute
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.timerText.text = "${secondsRemaining}s"
            }

            override fun onFinish() {
                Toast.makeText(requireContext(), "Time’s up! Returning to previous screen.", Toast.LENGTH_LONG).show()
                requireActivity().onBackPressed() // Navigate up
            }
        }
        countDownTimer.start()
    }
    private fun updateHealthDisplay() {
        binding.player1HealthText.text = "Your Health: $playerHealth"
        binding.player2HealthText.text = "Monster Health: $monsterHealth"

        binding.player1HealthBar.progress = playerHealth
        binding.player2HealthBar.progress = monsterHealth
    }
    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel() // Stop timer when view is destroyed
    }

    private fun showQuestionDialog() {
        val level = sharedViewModel.getCurrentLevel()
        val questions = level?.questions?.toMutableList() ?: mutableListOf()

        if (questions.isNotEmpty()) {
            val question = questions.random()
            Log.d("BattleFragment", "Question: ${question.questionText}, Choices: ${question.choices}")

            // Inflate the custom dialog view
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_question, null)
            val questionText = dialogView.findViewById<TextView>(R.id.questionText)
            val choicesGroup = dialogView.findViewById<RadioGroup>(R.id.choicesGroup)
            val submitButton = dialogView.findViewById<Button>(R.id.submitAnswerButton)

            // Set question text
            questionText.text = question.questionText

            // Add choices to RadioGroup
            question.choices.forEachIndexed { index, choice ->
                val radioButton = RadioButton(requireContext()).apply {
                    text = choice
                    id = index
                }
                choicesGroup.addView(radioButton)
            }

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()


            submitButton.setOnClickListener {
                val selectedId = choicesGroup.checkedRadioButtonId
                if (selectedId == -1) {
                    Toast.makeText(requireContext(), "Please select an answer.", Toast.LENGTH_SHORT).show()
                } else {
                    if (selectedId == question.correctAnswerIndex) {
                        // Correct answer logic
                        monsterHealth -= 20
                        Toast.makeText(requireContext(), "Correct! Monster's health decreases.", Toast.LENGTH_SHORT).show()

                        if (monsterHealth <= 0) {
                            playerPoints += 10
                            Toast.makeText(requireContext(), "Monster defeated! Points: $playerPoints", Toast.LENGTH_LONG).show()
                            resetGame()
                        } else {
                            performAttackAnimation(isPlayerAttacking = true)
                        }
                    } else {
                        // Incorrect answer logic
                        playerHealth -= 20
                        Toast.makeText(requireContext(), "Wrong! Your health decreases.", Toast.LENGTH_SHORT).show()

                        if (playerHealth <= 0) {
                            Toast.makeText(requireContext(), "You are defeated! Game Over!", Toast.LENGTH_LONG).show()
                            resetGame()
                        } else {
                            performAttackAnimationEnemy(isPlayerAttacking = true)  // Trigger enemy attack animation
                        }
                    }

                    questions.remove(question)

                    if (level?.questions?.isEmpty() == true) {
                        Toast.makeText(requireContext(), "Level Complete! Moving to next level.", Toast.LENGTH_SHORT).show()
                        resetGame()
                    }

                    updateHealthDisplay()
                    dialog.dismiss()
                }
            }

            dialog.show()

        } else {
            Toast.makeText(requireContext(), "No more questions. Level complete!", Toast.LENGTH_LONG).show()
            resetGame()
        }
    }
    private fun shakeScreen() {
        val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        binding.root.startAnimation(shake)
    }
    private fun performAttackAnimationEnemy(isPlayerAttacking: Boolean) {
        val attackId = if (isPlayerAttacking) {
            sharedViewModel.getSelectedCharacterAttackId() ?: -1 // Player's attack animation
        } else {
         //   R.drawable.monster_attack // Enemy's attack animation (replace with actual drawable)
        }

        if (attackId != -1) {
            // Display the attack animation
            Glide.with(this)
                .asGif()
                .load(attackId)
                .into(binding.player2Character)
            shakeScreen()
            // Revert back to character's idle/standing animation after delay
            Handler(Looper.getMainLooper()).postDelayed({
                sharedViewModel.getSelectedCharacterId()?.let { id ->
                    playGifEnemy(id)
                }
            }, 2000)
        }
    }
    private fun playGifEnemy(characterId: Int) {
        Glide.with(this)
            .asGif()
            .load(characterId)
            .into(binding.player2Character)
    }



    private fun performAttackAnimation(isPlayerAttacking: Boolean) {
        val attackId = if (isPlayerAttacking) {
            sharedViewModel.getSelectedCharacterAttackId() ?: -1
        } else {
           // R.drawable.monster_attack // Assuming you have an attack animation for the monster
        }

        if (attackId != -1) {
            Glide.with(this)
                .asGif()
                .load(attackId)
                .into(binding.player1Character)
            shakeScreen()
            Handler(Looper.getMainLooper()).postDelayed({
                sharedViewModel.getSelectedCharacterId()?.let { id ->
                    playGif(id)
                }
            }, 2000)
        }
    }

    private fun displaySelectedCharacterGif(characterId: Int) {
        Glide.with(this)
            .asGif()
            .load(characterId)
            .into(binding.player1Character)
    }
    private fun displaySelectedCharacterGifEnemy(characterId: Int) {
        Glide.with(this)
            .asGif()
            .load(characterId)
            .into(binding.player2Character)
    }

    private fun playGif(characterId: Int) {
        Glide.with(this)
            .asGif()
            .load(characterId)
            .into(binding.player1Character)
    }



    private fun resetGame() {
        // Reset health and show a Toast with final points
        monsterHealth = 100
        playerHealth = 100
        updateHealthDisplay()
    }
}
