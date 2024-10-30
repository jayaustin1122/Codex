package com.example.fightinggame.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fightinggame.R
import com.example.fightinggame.dao.CharacterDao
import com.example.fightinggame.dao.LevelsDao
import com.example.fightinggame.dao.MonsterEnemyDao
import com.example.fightinggame.dao.TriviaDao
import com.example.fightinggame.databinding.FragmentSplashBinding
import com.example.fightinggame.db.CodexDatabase
import com.example.fightinggame.model.Character
import com.example.fightinggame.model.MonsterEnemy
import com.example.fightinggame.model.Trivia
import com.example.fightinggame.model.mapsLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private var progressStatus = 0
    private lateinit var handler: Handler
    private lateinit var characterDao: CharacterDao
    private lateinit var monsterEnemyDao: MonsterEnemyDao
    private lateinit var levels: LevelsDao
    private lateinit var trivia: TriviaDao
    private lateinit var database: CodexDatabase
    private var isDataInserted = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        handler = Handler(Looper.getMainLooper())
        simulateProgress()
        // Restore the insertion state if it exists
        if (savedInstanceState != null) {
            isDataInserted = savedInstanceState.getBoolean("isDataInserted", false)
        }

        // Only insert data if it hasn't been inserted yet
        if (!isDataInserted) {
            insertDatas()
        }


        // Navigate to HomeFragment after 5 seconds
        handler.postDelayed({
            findNavController().navigate(R.id.homeFragment)
        }, 5000)
    }

    private fun insertDatas() {
        database = CodexDatabase(requireContext())
        characterDao = database.getCharacterDao()
        monsterEnemyDao = database.getMonsterEnemyDao()
        levels = database.getMapsLevelDao()
        trivia = database.getTriviaDao()

        lifecycleScope.launch(Dispatchers.IO) {
            // Insert characters
            val charactersToInsert = listOf(
                Character(1, "Hero1", 1, R.drawable.hero1, R.drawable.hero1_attack),
                Character(2, "Hero2", 1, R.drawable.hero2, R.drawable.hero1_attack),
                Character(3, "Hero3", 1, R.drawable.hero3, R.drawable.hero1_attack),
                Character(4, "Hero4", 1, R.drawable.hero4, R.drawable.hero1_attack)
            )

            // Check and insert characters
            for (character in charactersToInsert) {
                if (characterDao.getCharacterById(character.id) == null) { // Check for existing character
                    characterDao.insertCharacters(character) // Insert if not exists
                }
            }

            // Insert monsters
            val monstersToInsert = listOf(
                MonsterEnemy(1, 1, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(2, 2, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(3, 3, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(4, 4, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(5, 5, R.drawable.hero1, R.drawable.hero1_attack),
            )

            // Check and insert monsters
            for (monster in monstersToInsert) {
                if (monsterEnemyDao.getMonsterById(monster.id) == null) { // Check for existing monster
                    monsterEnemyDao.insertMonsterEnemy(monster) // Insert if not exists
                }
            }

            // Insert levels
            levels.insertLevels(
                mapsLevel(true, 1),
                mapsLevel(false, 2),
                mapsLevel(false, 3),
                mapsLevel(false, 4),
                mapsLevel(false, 5),
            )

            // Insert trivia
            insertTrivia(trivia)

            withContext(Dispatchers.Main) {
                Log.d("splash", "Inserted characters successfully")
                isDataInserted = true // Update flag after insertion
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the insertion state
        outState.putBoolean("isDataInserted", isDataInserted)
    }
    private fun simulateProgress() {
        handler.post(object : Runnable {
            override fun run() {
                if (progressStatus < 100) {
                    progressStatus += 2 // Increase progress gradually
                    binding.progressBar.progress = progressStatus
                    handler.postDelayed(this, 100) // Update every 100ms
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null) // Clean up handler to prevent memory leaks
    }
    val triviaList = listOf(
        Trivia(
            question = "What is the correct value to return to the operating system upon the successful completion of a program?",
            category = "C++",
            difficulty = "Easy",
            ans1 = "0",
            ans2 = "1",
            ans3 = "2",
            ans4 = "-1",
            number = 1,
            correctAnswerIndex = 1 // Answer is 0
        ),
        Trivia(
            question = "What symbol is used to state the beginning and the end of blocks of code?",
            category = "C++",
            difficulty = "Easy",
            ans1 = "{ and }",
            ans2 = "- and -",
            ans3 = "( and )",
            ans4 = "< and >",
            number = 2,
            correctAnswerIndex = 1 // Answer is { and }
        ),
        Trivia(
            question = "Which of the following answers is the correct operator to compare two variables?",
            category = "C++",
            difficulty = "Easy",
            ans1 = "==",
            ans2 = "&",
            ans3 = "=",
            ans4 = "&&",
            number = 3,
            correctAnswerIndex = 1 // Answer is ==
        ),
        Trivia(
            question = "Which of the following shows the correct syntax for an if statement?",
            category = "Java",
            difficulty = "Easy",
            ans1 = "if (expression)",
            ans2 = "if {expression}",
            ans3 = "if <expression>",
            ans4 = "if expression",
            number = 4,
            correctAnswerIndex = 1 // Answer is if (expression)
        ),
        Trivia(
            question = "Which of the following is the boolean operator for logical-and?",
            category = "Java",
            difficulty = "Easy",
            ans1 = "&&",
            ans2 = "|",
            ans3 = "==",
            ans4 = "&",
            number = 5,
            correctAnswerIndex = 1 // Answer is &&
        ),
        Trivia(
            question = "How do you say 'not equal to?'",
            category = "Java",
            difficulty = "Easy",
            ans1 = "!=",
            ans2 = "=!",
            ans3 = "==",
            ans4 = "=",
            number = 6,
            correctAnswerIndex = 1 // Answer is !=
        ),
        Trivia(
            question = "What symbol is used for not in C++?",
            category = "C++",
            difficulty = "Easy",
            ans1 = "!",
            ans2 = "^",
            ans3 = "*/",
            ans4 = "N",
            number = 7,
            correctAnswerIndex = 1 // Answer is !
        ),
        Trivia(
            question = "What does the cin object in C++ do?",
            category = "C++",
            difficulty = "Easy",
            ans1 = "Reads from the standard input",
            ans2 = "Reads from a file",
            ans3 = "Reads from a string",
            ans4 = "Reads from the standard output",
            number = 8,
            correctAnswerIndex = 1 // Answer is Reads from the standard input
        ),
        Trivia(
            question = "Which of the following is not a valid C++ data type?",
            category = "C++",
            difficulty = "Easy",
            ans1 = "real",
            ans2 = "int",
            ans3 = "char",
            ans4 = "float",
            number = 9,
            correctAnswerIndex = 1 // Answer is real
        ),
        Trivia(
            question = "What is a correct syntax to output 'Hello World' in Java?",
            category = "Java",
            difficulty = "Easy",
            ans1 = "System.out.println(Hello);",
            ans2 = "echo(Hello);",
            ans3 = "Console.WriteLine(Hello);",
            ans4 = "print(Hello);",
            number = 10,
            correctAnswerIndex = 1 // Answer is System.out.println(Hello);
        )
    )
    fun insertTrivia(triviaDao: TriviaDao) {
        triviaDao.insertTrivia(triviaList)
    }
}
