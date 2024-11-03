package com.example.fightinggame.ui

import android.app.AlertDialog
import android.media.MediaPlayer
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fightinggame.R
import com.example.fightinggame.dao.CharacterSelectionDao
import com.example.fightinggame.dao.LevelsDao
import com.example.fightinggame.dao.MonsterEnemyDao
import com.example.fightinggame.dao.UserPointsDao
import com.example.fightinggame.databinding.DialogQuestionBinding
import com.example.fightinggame.databinding.FragmentBattleBinding
import com.example.fightinggame.db.CodexDatabase
import com.example.fightinggame.model.TriviaQuestionUserAnswer
import com.example.fightinggame.model.UserPoints
import com.example.fightinggame.model.mapsLevel
import com.example.fightinggame.util.CharacterViewModelFactory
import com.example.fightinggame.util.SplashViewModelFactory
import com.example.fightinggame.viewmodels.CharacterViewModel
import com.example.fightinggame.viewmodels.SplashViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class BattleFragment : Fragment() {
    private lateinit var binding: FragmentBattleBinding
    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var characterSelectionDao: CharacterSelectionDao
    private lateinit var levelsDao: LevelsDao
    private lateinit var userPointsDao: UserPointsDao
    private lateinit var enemyDao: MonsterEnemyDao
    private var selectedIndex: Int? = null
    private val viewModel: SplashViewModel by viewModels { SplashViewModelFactory(requireContext()) }
    private var mediaPlayer: MediaPlayer? = null
    // Variables to track health and points
    private var monsterHealth = 100
    private var playerHealth = 100
    private var playerPoints = 0
    private lateinit var countDownTimer: CountDownTimer
    private var questionDialog: AlertDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBattleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTimer()
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.battle)
        mediaPlayer?.isLooping = true // To loop the music
        mediaPlayer?.start()
        selectedIndex = arguments?.getInt("selected_level_index")
        val selectedIndex = arguments?.getInt("selected_level_index") ?: 0

        when (selectedIndex) {
            1 -> Glide.with(this).asGif().load(R.drawable.bgbattle4).into(binding.battleBackground)
            2 -> Glide.with(this).asGif().load(R.drawable.bgbattle2).into(binding.battleBackground)
            3 -> Glide.with(this).asGif().load(R.drawable.bgbattle3).into(binding.battleBackground)
            4 -> Glide.with(this).asGif().load(R.drawable.bgbattle10).into(binding.battleBackground)
            5 -> Glide.with(this).asGif().load(R.drawable.bgbattle5).into(binding.battleBackground)
            6 -> Glide.with(this).asGif().load(R.drawable.bgbattle6).into(binding.battleBackground)
            7 -> Glide.with(this).asGif().load(R.drawable.bgbattle7).into(binding.battleBackground)
            8 -> Glide.with(this).asGif().load(R.drawable.bgbattle8).into(binding.battleBackground)
            9 -> Glide.with(this).asGif().load(R.drawable.bgbattle9).into(binding.battleBackground)
            10 -> Glide.with(this).asGif().load(R.drawable.bgbattle1).into(binding.battleBackground)
            else -> Glide.with(this).asGif().load(R.drawable.bgbattle1).into(binding.battleBackground)
        }

        // Initialize database DAOs
        val characterDao = CodexDatabase.invoke(requireContext()).getCharacterDao()
        characterSelectionDao = CodexDatabase.invoke(requireContext()).getCharacterSelectionDao()
        enemyDao = CodexDatabase.invoke(requireContext()).getMonsterEnemyDao()
        levelsDao = CodexDatabase.invoke(requireContext()).getMapsLevelDao()
        userPointsDao = CodexDatabase.invoke(requireContext()).getUserPointsDao()

        // Initialize ViewModel with factory
        val factory = CharacterViewModelFactory(characterDao)
        characterViewModel = ViewModelProvider(this, factory).get(CharacterViewModel::class.java)

        loadCharactersFromDatabase()
        loadEnemyFromDatabase(selectedIndex)
        updateHealthDisplay()

        // Start battle interaction
        binding.player1Character.setOnClickListener {
            showQuestionDialog()

        }
    }

    private fun loadEnemyFromDatabase(selectedIndex: Int?) {
        viewLifecycleOwner.lifecycleScope.launch {
            selectedIndex?.let { index ->
                val enemy = enemyDao.getMonsterById(index)
                enemy?.let {
                    displaySelectedCharacterGifEnemy(it.gifStand!!)
                }
            }
        }
    }

    private fun loadCharactersFromDatabase() {
        viewLifecycleOwner.lifecycleScope.launch {
            val character = characterSelectionDao.getCharacterSelectionById(1)
            character?.let {
                displaySelectedCharacterGif(it.gifStand!!)
            }
        }
    }

    private fun updateHealthDisplay() {
        binding.player1HealthText.text = "Your Health: $playerHealth"
        binding.player2HealthText.text = "Enemy Health: $monsterHealth"

        binding.player1HealthBar.progress = playerHealth
        binding.player2HealthBar.progress = monsterHealth
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel() // Stop timer when view is destroyed
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private val askedQuestionNumbers = mutableListOf<Int>()
    private val listOfQuestionAnswers = mutableListOf<TriviaQuestionUserAnswer>()
    private fun showQuestionDialog() {
        lifecycleScope.launch {
            if (isAdded) {
                val triviaDao = CodexDatabase.invoke(requireContext()).getTriviaDao()
                val questions = triviaDao.getRandomNewTrivia()

                if (questions.isNotEmpty()) {
                    val question = questions.random()

                    // Store the question number if it hasn't been asked
                    if (!askedQuestionNumbers.contains(question.number)) {
                        askedQuestionNumbers.add(question.number)
                    }

                    val dialogBinding =
                        DialogQuestionBinding.inflate(LayoutInflater.from(requireContext()))
                    dialogBinding.questionText.text = question.question

                    // Randomize the answers
                    val answers = listOf(
                        question.ans1, question.ans2, question.ans3, question.ans4
                    ).shuffled()

                    dialogBinding.answerButton1.text = answers[0]
                    dialogBinding.answerButton2.text = answers[1]
                    dialogBinding.answerButton3.text = answers[2]
                    dialogBinding.answerButton4.text = answers[3]

                    questionDialog =
                        AlertDialog.Builder(requireContext()).setView(dialogBinding.root)
                            .setCancelable(false).create()

                    // Set click listeners for the answer buttons
                    val answerButtons = listOf(
                        dialogBinding.answerButton1,
                        dialogBinding.answerButton2,
                        dialogBinding.answerButton3,
                        dialogBinding.answerButton4
                    )

                    answerButtons.forEach { button ->
                        button.setOnClickListener {
                            questionDialog?.dismiss() // Dismiss the dialog when an answer is clicked
                            val healthDeduction = when (question.difficulty) {
                                "Easy" -> 10
                                "Medium" -> 20
                                "Hard" -> 30
                                else -> 0
                            }
                            // Check if the clicked button text matches the correct answer
                            if (button.text == question.correctAnswerIndex) {
                                monsterHealth -= healthDeduction
                                Toast.makeText(
                                    requireContext(),
                                    "Correct! Monster's health decreases by $healthDeduction.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                playerPoints++ // Increment points
                                Log.d(
                                    "PlayerPoints",
                                    "Current Player Points: $playerPoints"
                                ) // Log the updated points

                                shakeScreen()
                                viewLifecycleOwner.lifecycleScope.launch {
                                    triviaDao.markQuestionAsAskedById(question.number)
                                    Log.d(
                                        "BattleFragment",
                                        "Question marked as asked: ${question.question}"
                                    )
                                    listOfQuestionAnswers.add(
                                        TriviaQuestionUserAnswer(
                                            question = question.question,
                                            correctAnswerIndex = question.correctAnswerIndex,
                                            number = question.number,
                                            ans1 = question.ans1,
                                            ans2 = question.ans2,
                                            ans3 = question.ans3,
                                            ans4 = question.ans4,
                                            level = selectedIndex!!.toInt(),
                                            userSelectAnswer = button.text.toString().trim()
                                        )
                                    )
                                }

                                if (monsterHealth <= 0) {
                                    lifecycleScope.launch {
                                        try {
                                            // Step 1: Update the player's level
                                            val level = selectedIndex?.plus(1)?.let { it1 ->
                                                mapsLevel(
                                                    true,
                                                    false,
                                                    it1.toLong()
                                                )
                                            }
                                            level?.let {
                                                levelsDao.updateLevel(it)
                                                Log.d(
                                                    "BattleFragment",
                                                    "Level updated successfully."
                                                )
                                            }
                                            listOfQuestionAnswers.forEach { answer ->
                                                Log.d(
                                                    "BattleFragment",
                                                    "Inserting question answer: ${answer.question}, " +
                                                            "User's answer: ${answer.userSelectAnswer}, " +
                                                            "Correct answer: ${answer.correctAnswerIndex}"
                                                )
                                            }

                                                // Store all questions and answers into the database
                                            viewModel.insertQuestionsAnswer2(listOfQuestionAnswers)
                                            // Step 3: Navigate to the review fragment with updated index


                                            // Step 4: Retrieve the current user points from the database
                                            val existingPoints = userPointsDao.getUserPoints(1)
                                            if (existingPoints != null) {
                                                // Add the new points to the existing points
                                                val updatedPoints =
                                                    existingPoints.points + playerPoints
                                                userPointsDao.updateUserPoints(
                                                    UserPoints(
                                                        1,
                                                        updatedPoints
                                                    )
                                                )
                                                findNavController().navigate(R.id.mapsFragment)

                                                Toast.makeText(
                                                    requireContext(),
                                                    "Points updated: $updatedPoints",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                Log.d(
                                                    "BattleFragment",
                                                    "User points updated successfully: Previous: ${existingPoints.points}, New: $playerPoints"
                                                )
                                                val adjustedIndex = selectedIndex?.plus(1)
                                                val bundle = Bundle().apply {
                                                    putInt("index", adjustedIndex!!)
                                                }
                                                findNavController().navigate(
                                                    R.id.reviewFragment,
                                                    bundle
                                                )
                                            } else {
                                                // If no points exist, insert the new points
                                                userPointsDao.insertUserPoints(
                                                    UserPoints(
                                                        1,
                                                        playerPoints
                                                    )
                                                )
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Points inserted: $playerPoints",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                Log.d(
                                                    "BattleFragment",
                                                    "User points inserted successfully."
                                                )
                                            }

                                            // Step 5: Update the current level status to 'finished'
                                            levelsDao.updateLevel(
                                                mapsLevel(
                                                    true,
                                                    true,
                                                    selectedIndex!!.toLong()
                                                )
                                            )
                                            Log.d(
                                                "BattleFragment",
                                                "Current level status updated to finished."
                                            )

                                        } catch (e: CancellationException) {
                                            Log.e(
                                                "BattleFragment",
                                                "Coroutine was cancelled: ${e.message}",
                                                e
                                            )
                                        } catch (e: Exception) {
                                            Log.e(
                                                "BattleFragment",
                                                "Error during updates: ${e.message}",
                                                e
                                            )
                                        } finally {
                                            // Step 6: Navigate back and reset the game after everything is done
                                            findNavController().navigate(R.id.mapsFragment)

                                        }
                                    }
                                } else {
                                    // If the monster is not defeated, continue with the attack animation
                                    performAttackAnimation(isPlayerAttacking = true)
                                }


                            } else {
                                playerHealth -= healthDeduction
                                Toast.makeText(
                                    requireContext(),
                                    "Wrong! Your health decreases by $healthDeduction.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                listOfQuestionAnswers.add(
                                    TriviaQuestionUserAnswer(
                                        question = question.question,
                                        correctAnswerIndex = question.correctAnswerIndex,
                                        number = question.number,
                                        ans1 = question.ans1,
                                        ans2 = question.ans2,
                                        ans3 = question.ans3,
                                        ans4 = question.ans4,
                                        level = selectedIndex!!.toInt(),
                                        userSelectAnswer = button.text.toString().trim()
                                    )
                                )
                                shakeScreen()

                                if (playerHealth <= 0) {
                                    Toast.makeText(
                                        requireContext(),
                                        "You are defeated! Please Try Again!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    findNavController().navigate(R.id.mapsFragment)

                                    return@setOnClickListener
                                } else {
                                    performAttackAnimationEnemy(isPlayerAttacking = true)
                                }
                            }

                            // Update health display and show the next question
                            updateHealthDisplay()
                            Handler(Looper.getMainLooper()).postDelayed({
                                showQuestionDialog()
                            }, 2000)
                        }
                    }

                    questionDialog?.show() // Show the dialog
                } else {
                    Toast.makeText(requireContext(), "No more questions.", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }

        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(60000, 1000) { // 1 minute
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.timerText.text = "${secondsRemaining}s"
            }

            override fun onFinish() {
                Toast.makeText(
                    requireContext(), "Time’s up! Please Try Again.", Toast.LENGTH_LONG
                ).show()
                questionDialog?.dismiss() // Dismiss the dialog if it's showing
                findNavController().navigateUp()
            }
        }
        countDownTimer.start()
    }

    private fun displaySelectedCharacterGif(characterId: Int) {
        Glide.with(this).asGif().load(characterId).into(binding.player1Character)
    }

    private fun displaySelectedCharacterGifEnemy(characterId: Int) {
        Glide.with(this).asGif().load(characterId).into(binding.player2Character)
    }

    private fun shakeScreen() {
        val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        binding.root.startAnimation(shake)
    }

    private fun performAttackAnimationEnemy(isPlayerAttacking: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            // Get the selected character's information
            val character = enemyDao.getMonsterById(selectedIndex!!)
            character?.let {
                // Display attack animation for the player
                displaySelectedCharacterGifAttackEnemy(it.gifAttack!!)
                Handler(Looper.getMainLooper()).postDelayed({
                    displaySelectedCharacterGifEnemy(it.gifStand!!)
                }, 2000)
            }
        }
    }

    private fun displaySelectedCharacterGifAttackEnemy(gifAttack: Int) {
        Glide.with(this).asGif().load(gifAttack).into(binding.player2Character)
    }

    private fun performAttackAnimation(isPlayerAttacking: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            // Get the selected character's information
            val character = characterSelectionDao.getCharacterSelectionById(1)
            character?.let {
                // Display attack animation for the player
                displaySelectedCharacterGifAttack(it.gifAttack!!)
                Handler(Looper.getMainLooper()).postDelayed({
                    displaySelectedCharacterGif(it.gifStand!!)
                }, 2000)
            }
        }
    }

    private fun displaySelectedCharacterGifAttack(gifAttack: Int) {
        Glide.with(this).asGif().load(gifAttack).into(binding.player1Character)
    }
}



