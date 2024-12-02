package com.example.fightinggame.viewmodels


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fightinggame.R
import com.example.fightinggame.dao.CharacterDao
import com.example.fightinggame.dao.LevelsDao
import com.example.fightinggame.dao.MonsterEnemyDao
import com.example.fightinggame.dao.TriviaDao
import com.example.fightinggame.dao.TriviaUserAnswerDao
import com.example.fightinggame.dao.UserDao
import com.example.fightinggame.dao.UserPointsDao
import com.example.fightinggame.db.CodexDatabase
import com.example.fightinggame.model.Character
import com.example.fightinggame.model.MonsterEnemy
import com.example.fightinggame.model.Trivia
import com.example.fightinggame.model.TriviaQuestionUserAnswer
import com.example.fightinggame.model.User
import com.example.fightinggame.model.UserPoints
import com.example.fightinggame.model.mapsLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(private val context: Context) : ViewModel() {

    private lateinit var characterDao: CharacterDao
    private lateinit var monsterEnemyDao: MonsterEnemyDao
    private lateinit var levelsDao: LevelsDao
    private lateinit var triviaDao: TriviaDao
    private lateinit var userDao: UserDao
    private lateinit var userPointsDao: UserPointsDao
    private lateinit var triviaUserAnswerDao: TriviaUserAnswerDao
    private var _isDataInserted = MutableLiveData<Boolean>()
    val isDataInserted: LiveData<Boolean> get() = _isDataInserted

    init {
        val database = CodexDatabase(context)
        characterDao = database.getCharacterDao()
        monsterEnemyDao = database.getMonsterEnemyDao()
        levelsDao = database.getMapsLevelDao()
        triviaDao = database.getTriviaDao()
        userDao = database.getUserName()
        userPointsDao = database.getUserPointsDao()
        triviaUserAnswerDao = database.getTriviaQuestionUserAnswer()
    }
    fun insertUser(user: User) {
        viewModelScope.launch {
            userDao.insertUser(user)
        }
    }
    fun deleteAllAnswers(){
        viewModelScope.launch {
            triviaUserAnswerDao.deleteAllTriviaQuestions()
        }
    }
    fun insertQuestionsAnswer(trivias: TriviaQuestionUserAnswer) {
        viewModelScope.launch {
            triviaUserAnswerDao.insertTriviaQuestion(trivias)
        }
    }
    fun insertQuestionsAnswer2(trivias: List<TriviaQuestionUserAnswer>) {
        viewModelScope.launch {
            triviaUserAnswerDao.insertMultipleTriviaQuestions(trivias)
        }
    }


    fun updatePoints(userPoints: UserPoints) {
        viewModelScope.launch {
            userPointsDao.updateUserPoints(userPoints)
        }
    }

    fun insertData() {
        viewModelScope.launch(Dispatchers.IO) {
            val charactersToInsert = listOf(
                Character(1, "Prince", 1, R.drawable.hero1, R.drawable.hero1_attack),
                Character(2, "Boy-Boy", 1, R.drawable.boy_stance, R.drawable.boy_attack),
                Character(3, "Hero Gold", 1, R.drawable.golden_knight_stance, R.drawable.golden_knight_attack),
                Character(4, "Mage", 1, R.drawable.mage_stance, R.drawable.mage_attack)
            )

            // Insert characters
            for (character in charactersToInsert) {
                if (characterDao.getCharacterById(character.id) == null) {
                    characterDao.insertCharacters(character)
                }
            }

            val monstersToInsert = listOf(
                MonsterEnemy(1, 1, R.drawable.monster1_stance, R.drawable.monster1_attack),
                MonsterEnemy(2, 2, R.drawable.tree_monster_attack, R.drawable.tree_monster_attack),
                MonsterEnemy(3, 3, R.drawable.silver_knight_stance, R.drawable.silver_knight_attack),
                MonsterEnemy(4, 4, R.drawable.knight_stance, R.drawable.knight_attack),
                MonsterEnemy(5, 5, R.drawable.girl2_stance, R.drawable.girl2_attack),
                MonsterEnemy(6, 6, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(7, 7, R.drawable.boy_stance, R.drawable.boy_attack),
                MonsterEnemy(8, 8, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(9, 9, R.drawable.mage_stance, R.drawable.mage_attack),
                MonsterEnemy(10, 10, R.drawable.dragon_stance, R.drawable.dragon_attack),
                MonsterEnemy(11, 11, R.drawable.monster1_stance, R.drawable.monster1_attack),
                MonsterEnemy(12, 12, R.drawable.tree_monster_attack, R.drawable.tree_monster_attack),
                MonsterEnemy(13, 13, R.drawable.silver_knight_stance, R.drawable.silver_knight_attack),
                MonsterEnemy(14, 14, R.drawable.knight_stance, R.drawable.knight_attack),
                MonsterEnemy(15, 15, R.drawable.girl2_stance, R.drawable.girl2_attack),
                MonsterEnemy(16, 16, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(17, 17, R.drawable.boy_stance, R.drawable.boy_attack),
                MonsterEnemy(18, 18, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(19, 19, R.drawable.mage_stance, R.drawable.mage_attack),
                MonsterEnemy(20, 20, R.drawable.dragon_stance, R.drawable.dragon_attack),
                MonsterEnemy(21, 21, R.drawable.monster1_stance, R.drawable.monster1_attack),
                MonsterEnemy(22, 22, R.drawable.tree_monster_attack, R.drawable.tree_monster_attack),
                MonsterEnemy(23, 23, R.drawable.silver_knight_stance, R.drawable.silver_knight_attack),
                MonsterEnemy(24, 24, R.drawable.knight_stance, R.drawable.knight_attack),
                MonsterEnemy(25, 25, R.drawable.girl2_stance, R.drawable.girl2_attack),
                MonsterEnemy(26, 26, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(27, 27, R.drawable.boy_stance, R.drawable.boy_attack),
                MonsterEnemy(28, 28, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(29, 29, R.drawable.mage_stance, R.drawable.mage_attack),
                MonsterEnemy(30, 30, R.drawable.dragon_stance, R.drawable.dragon_attack),
            )

            // Insert monsters
            for (monster in monstersToInsert) {
                if (monsterEnemyDao.getMonsterById(monster.id) == null) {
                    monsterEnemyDao.insertMonsterEnemy(monster)
                }
            }

            // Insert levels
            levelsDao.insertLevels(
                mapsLevel(true, false, 1),
                mapsLevel(false, false, 2),
                mapsLevel(false, false, 3),
                mapsLevel(false, false, 4),
                mapsLevel(false, false, 5),
                mapsLevel(false, false, 6),
                mapsLevel(false, false, 7),
                mapsLevel(false, false, 8),
                mapsLevel(false, false, 9),
                mapsLevel(false, false, 11),
                mapsLevel(false, false, 12),
                mapsLevel(false, false, 13),
                mapsLevel(false, false, 14),
                mapsLevel(false, false, 15),
                mapsLevel(false, false, 16),
                mapsLevel(false, false, 17),
                mapsLevel(false, false, 18),
                mapsLevel(false, false, 19),
                mapsLevel(false, false, 20),
                mapsLevel(false, false, 21),
                mapsLevel(false, false, 22),
                mapsLevel(false, false, 23),
                mapsLevel(false, false, 24),
                mapsLevel(false, false, 25),
                mapsLevel(false, false, 26),
                mapsLevel(false, false, 27),
                mapsLevel(false, false, 28),
                mapsLevel(false, false, 29),
                mapsLevel(false, false, 30),
            )

            // Insert trivia
            insertTrivia(triviaDao)

            withContext(Dispatchers.Main) {
                _isDataInserted.value = true
            }
        }
    }

    private fun insertTrivia(triviaDao: TriviaDao) {
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
                correctAnswerIndex = "0",
                isIdentification = true
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
                correctAnswerIndex = "{ and }",
                isIdentification = false
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
                correctAnswerIndex = "==",
                isIdentification = true
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
                correctAnswerIndex = "if (expression)",
                isIdentification = false // Answer is if (expression)
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
                correctAnswerIndex = "&&", // Answer is &&
                isIdentification = true
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
                correctAnswerIndex = "!=", // Answer is !=
                isIdentification = true
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
                correctAnswerIndex = "!", // Answer is !
                isIdentification = true
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
                correctAnswerIndex = "Reads from the standard input" // Answer is Reads from the standard input
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
                correctAnswerIndex = "real", // Answer is real,
                        isIdentification = false
            ),
            Trivia(
                question = "Which data type is used to create a variable that should store text?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "string",
                ans2 = "Mystring",
                ans3 = "char",
                ans4 = "txt",
                number = 11,
                correctAnswerIndex = "string",
                isIdentification = true// Answer is string
            ),
            Trivia(
                question = "To declare an array in Java, define the variable type with:",
                category = "C++",
                difficulty = "Easy",
                ans1 = "[]",
                ans2 = "<>",
                ans3 = "{}",
                ans4 = "()",
                number = 12,
                correctAnswerIndex = "[]",
                isIdentification = true// Answer is []
            ),
            Trivia(
                question = "Which operator is used to multiply numbers?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "*",
                ans2 = "#",
                ans3 = "/",
                ans4 = "%",
                number = 13,
                correctAnswerIndex = "*",
                        isIdentification = true// Answer is *
            ),
            Trivia(
                question = "Identify the operator used to access a member of a class or struct in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = ".",
                ans2 = "->",
                ans3 = "#",
                ans4 = "&",
                number = 1,
                correctAnswerIndex = ".",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to define a class in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "class",
                ans2 = "struct",
                ans3 = "object",
                ans4 = "function",
                number = 2,
                correctAnswerIndex = "class",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the type of error that occurs when a program tries to use a variable that has not been initialized.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "Undefined behavior",
                ans2 = "Runtime error",
                ans3 = "Compile-time error",
                ans4 = "Logical error",
                number = 3,
                correctAnswerIndex = "Undefined behavior",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the header file required to use the sqrt() function in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "#include <cmath>",
                ans2 = "#include <math.h>",
                ans3 = "#include <sqrt.h>",
                ans4 = "#include <stdlib.h>",
                number = 4,
                correctAnswerIndex = "#include <cmath>",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the data type that can store large numbers with a greater range in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "long long",
                ans2 = "int",
                ans3 = "float",
                ans4 = "double",
                number = 5,
                correctAnswerIndex = "long long",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the correct way to declare a pointer in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "int* ptr;",
                ans2 = "int ptr;",
                ans3 = "ptr int;",
                ans4 = "pointer int;",
                number = 6,
                correctAnswerIndex = "int* ptr;",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the symbol used for the 'address of' operator in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "&",
                ans2 = "#",
                ans3 = "*",
                ans4 = "%",
                number = 7,
                correctAnswerIndex = "&",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the C++ function used to terminate the program.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "exit()",
                ans2 = "quit()",
                ans3 = "terminate()",
                ans4 = "end()",
                number = 8,
                correctAnswerIndex = "exit()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the type of loop that executes at least once even if the condition is false.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "while loop",
                ans2 = "for loop",
                ans3 = "do-while loop",
                ans4 = "forever loop",
                number = 9,
                correctAnswerIndex = "do-while loop",
                isIdentification = true
            ),
            Trivia(
                question = "Identify the function used to round a floating-point number to the nearest integer in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "round()",
                ans2 = "ceil()",
                ans3 = "floor()",
                ans4 = "abs()",
                number = 10,
                correctAnswerIndex = "round()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the data type used to store a boolean value in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "bool",
                ans2 = "int",
                ans3 = "char",
                ans4 = "byte",
                number = 11,
                correctAnswerIndex = "bool",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the operator that is used to get the value stored at a memory address in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "&",
                ans2 = "*",
                ans3 = "#",
                ans4 = "%",
                number = 12,
                correctAnswerIndex = "*",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the type of error that occurs when a program attempts to divide by zero in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "Runtime error",
                ans2 = "Compile-time error",
                ans3 = "Undefined behavior",
                ans4 = "Logic error",
                number = 13,
                correctAnswerIndex = "Runtime error",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the syntax for defining a class constructor in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "ClassName() {}",
                ans2 = "ClassName() ;",
                ans3 = "void ClassName() {}",
                ans4 = "constructor ClassName() {}",
                number = 14,
                correctAnswerIndex = "ClassName() {}",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to create a new object in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "new",
                ans2 = "create",
                ans3 = "object",
                ans4 = "instance",
                number = 15,
                correctAnswerIndex = "new",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to access the parent class constructor in C++ if there is an inheritance relationship.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "super",
                ans2 = "parent",
                ans3 = "base",
                ans4 = "this",
                number = 16,
                correctAnswerIndex = "super",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the data type used to store a sequence of characters in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "char",
                ans2 = "string",
                ans3 = "string[]",
                ans4 = "text",
                number = 17,
                correctAnswerIndex = "string",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the function used to find the length of a string in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "length()",
                ans2 = "size()",
                ans3 = "length() or size()",
                ans4 = "getSize()",
                number = 18,
                correctAnswerIndex = "length() or size()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the type of loop in C++ that checks the condition before the loop body is executed.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "for loop",
                ans2 = "while loop",
                ans3 = "do-while loop",
                ans4 = "forever loop",
                number = 19,
                correctAnswerIndex = "while loop",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the scope resolution operator in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "::",
                ans2 = "->",
                ans3 = ".",
                ans4 = "&",
                number = 20,
                correctAnswerIndex = "::",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to declare a reference variable in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "&",
                ans2 = "*",
                ans3 = "ref",
                ans4 = "address",
                number = 21,
                correctAnswerIndex = "&",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to define a pointer in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "*",
                ans2 = "&",
                ans3 = "pointer",
                ans4 = "address",
                number = 22,
                correctAnswerIndex = "*",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the C++ operator that is used to concatenate two strings.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "+",
                ans2 = "&",
                ans3 = "append()",
                ans4 = ".concat()",
                number = 23,
                correctAnswerIndex = "+",
                isIdentification = true
            ),
            Trivia(
                question = "Identify the function used to compare two strings in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "strcmp()",
                ans2 = "compare()",
                ans3 = "strcompare()",
                ans4 = "equal()",
                number = 24,
                correctAnswerIndex = "strcmp()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to define a derived class in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "class",
                ans2 = "derived",
                ans3 = "subclass",
                ans4 = "extends",
                number = 25,
                correctAnswerIndex = "class",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the function used to find the square root of a number in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "sqrt()",
                ans2 = "squareRoot()",
                ans3 = "pow()",
                ans4 = "root()",
                number = 26,
                correctAnswerIndex = "sqrt()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the type of inheritance where a class can inherit from multiple classes in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "Multiple inheritance",
                ans2 = "Single inheritance",
                ans3 = "Multilevel inheritance",
                ans4 = "Hybrid inheritance",
                number = 27,
                correctAnswerIndex = "Multiple inheritance",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the type of memory allocation used when using new in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "Dynamic memory allocation",
                ans2 = "Static memory allocation",
                ans3 = "Automatic memory allocation",
                ans4 = "Stack memory allocation",
                number = 28,
                correctAnswerIndex = "Dynamic memory allocation",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to declare a class that cannot be inherited in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "final",
                ans2 = "sealed",
                ans3 = "const",
                ans4 = "immutable",
                number = 29,
                correctAnswerIndex = "final",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the operator used for logical AND in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "&&",
                ans2 = "and",
                ans3 = "||",
                ans4 = "&",
                number = 30,
                correctAnswerIndex = "&&",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the function used to convert a string to an integer in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "stoi()",
                ans2 = "toInt()",
                ans3 = "convert()",
                ans4 = "parseInt()",
                number = 31,
                correctAnswerIndex = "stoi()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the function used to get the size of an array in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "sizeof()",
                ans2 = "length()",
                ans3 = "arraySize()",
                ans4 = "sizeOfArray()",
                number = 32,
                correctAnswerIndex = "sizeof()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the name of the C++ operator used to access a member of a class through a pointer.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "->",
                ans2 = ".",
                ans3 = "::",
                ans4 = "&",
                number = 33,
                correctAnswerIndex = "->",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to create a constant reference to a value in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "const&",
                ans2 = "reference&",
                ans3 = "const",
                ans4 = "readonly&",
                number = 34,
                correctAnswerIndex = "const&",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the operator that is used to check inequality in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "!=",
                ans2 = "!==",
                ans3 = "notEqual()",
                ans4 = "not==",
                number = 35,
                correctAnswerIndex = "!=",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the function used to round a floating-point number up to the nearest integer in C++.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "ceil()",
                ans2 = "floor()",
                ans3 = "round()",
                ans4 = "up()",
                number = 36,
                correctAnswerIndex = "ceil()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the C++ function used to convert a string to a floating-point number.",
                category = "C++",
                difficulty = "Easy",
                ans1 = "stof()",
                ans2 = "stod()",
                ans3 = "toFloat()",
                ans4 = "stringToFloat()",
                number = 37,
                correctAnswerIndex = "stof()",
                isIdentification = true
            ),
            Trivia(
                question = "Identify the class used to handle input in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Scanner",
                ans2 = "InputStream",
                ans3 = "BufferedReader",
                ans4 = "Console",
                number = 38,
                correctAnswerIndex = "Scanner",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the access modifier that allows access only within the same package in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "protected",
                ans2 = "private",
                ans3 = "public",
                ans4 = "default",
                number = 39,
                correctAnswerIndex = "protected",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to declare a constant value in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "final",
                ans2 = "const",
                ans3 = "constant",
                ans4 = "immutable",
                number = 40,
                correctAnswerIndex = "final",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the method used to return the absolute value of a number in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Math.abs()",
                ans2 = "abs()",
                ans3 = "Math.absolute()",
                ans4 = "absValue()",
                number = 41,
                correctAnswerIndex = "Math.abs()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the Java loop that executes at least once before checking the condition.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "do-while",
                ans2 = "while",
                ans3 = "for",
                ans4 = "repeat",
                number = 42,
                correctAnswerIndex = "do-while",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the class in Java used for mathematical calculations, like power and square root.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Math",
                ans2 = "BigDecimal",
                ans3 = "NumberUtils",
                ans4 = "MathUtils",
                number = 43,
                correctAnswerIndex = "Math",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the type of error that occurs when you try to divide by zero in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "ArithmeticException",
                ans2 = "ZeroDivisionError",
                ans3 = "DivideByZeroException",
                ans4 = "MathError",
                number = 44,
                correctAnswerIndex = "ArithmeticException",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to declare a class in Java that cannot be inherited.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "final",
                ans2 = "static",
                ans3 = "sealed",
                ans4 = "abstract",
                number = 45,
                correctAnswerIndex = "final",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to create an object in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "new",
                ans2 = "create",
                ans3 = "instantiate",
                ans4 = "object",
                number = 46,
                correctAnswerIndex = "new",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the function used to round a number to the nearest integer in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Math.round()",
                ans2 = "round()",
                ans3 = "roundToInt()",
                ans4 = "Math.floor()",
                number = 47,
                correctAnswerIndex = "Math.round()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used for defining an interface in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "interface",
                ans2 = "implements",
                ans3 = "abstract",
                ans4 = "class",
                number = 48,
                correctAnswerIndex = "interface",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the type of memory allocation used when creating objects in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Dynamic memory allocation",
                ans2 = "Static memory allocation",
                ans3 = "Automatic memory allocation",
                ans4 = "Stack memory allocation",
                number = 49,
                correctAnswerIndex = "Dynamic memory allocation",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the default value of a boolean variable in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "false",
                ans2 = "true",
                ans3 = "0",
                ans4 = "null",
                number = 50,
                correctAnswerIndex = "false",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the exception type thrown when trying to access an invalid array index in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "ArrayIndexOutOfBoundsException",
                ans2 = "IndexOutOfRangeException",
                ans3 = "ArrayOutOfBoundsException",
                ans4 = "InvalidArrayIndexException",
                number = 51,
                correctAnswerIndex = "ArrayIndexOutOfBoundsException",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the method used to convert an integer to a string in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "String.valueOf()",
                ans2 = "Integer.toString()",
                ans3 = "convert()",
                ans4 = "intToString()",
                number = 52,
                correctAnswerIndex = "String.valueOf()",
                isIdentification = true
            ),

                    Trivia(
                question = "Which keyword is used to return a value inside a method?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "return",
                ans2 = "break",
                ans3 = "void",
                ans4 = "get",
                number = 14,
                correctAnswerIndex = "return",
                isIdentification = true// Answer is return
            ),
            Trivia(
                question = "Which statement is used to stop a loop?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "break",
                ans2 = "stop",
                ans3 = "return",
                ans4 = "end",
                number = 15,
                correctAnswerIndex = "break",
                isIdentification = true// Answer is break
            ),
            Trivia(
                question = "Reverse a string in C++.",
                category = "C++",
                difficulty = "Hard",
                ans1 = "std::reverse(s.begin(), s.end());",
                ans2 = "for(i=s.length()-1; i>=0)",
                ans3 = "reverse_iterator",
                ans4 = "s.rbegin() to s.rend()",
                number = 16,
                correctAnswerIndex = "std::reverse(s.begin(), s.end());",
                isIdentification = false// Answer is std::reverse(s.begin(), s.end())
            ),
            Trivia(
                question = "Encapsulation for an employee in Java.",
                category = "Java",
                difficulty = "Hard",
                ans1 = "private String name; ... getters and setters",
                ans2 = "Employee(String name, age)",
                ans3 = "this.name.equals(name)",
                ans4 = "super(name, age)",
                number = 17,
                correctAnswerIndex = "private String name; ... getters and setters" // Answer is private String name; ... getters and setters
                ,
                isIdentification = false
            ),
            Trivia(
                question = "Factorial using recursion in C++.",
                category = "C++",
                difficulty = "Hard",
                ans1 = "int factorial(int n) { return n * factorial(n - 1); }",
                ans2 = "while(n!=0)",
                ans3 = "n + factorial(1);",
                ans4 = "factorial(n + 1);",
                number = 18,
                correctAnswerIndex = "int factorial(int n) { return n * factorial(n - 1); }" // Answer is int factorial(int n) { return n * factorial(n - 1); }.
                ,
                isIdentification = false
            ),
            Trivia(
                question = "Implement a thread using Runnable in Java.",
                category = "Java",
                difficulty = "Hard",
                ans1 = "class MyThread implements Runnable { run() {...} }",
                ans2 = "ThreadExample {...}",
                ans3 = "startThread()",
                ans4 = "void threadMain()",
                number = 19,
                correctAnswerIndex = "class MyThread implements Runnable { run() {...} }" // Answer is class MyThread implements Runnable { run() {...} }
                ,
                isIdentification = false
            ),
            Trivia(
                question = "Swap two numbers using pointers in C++.",
                category = "C++",
                difficulty = "Hard",
                ans1 = "void swap(int *x, int *y) {...}",
                ans2 = "pointerSwap(int& x, int& y)",
                ans3 = "swap(x, y)",
                ans4 = "std::swap(x, y)",
                number = 20,
                correctAnswerIndex = "void swap(int *x, int *y) {...}" // Answer is void swap(int *x, int *y) {...}
                ,
                isIdentification = false
            ),
            Trivia(
                question = "Implement interface for rectangle in Java.",
                category = "Java",
                difficulty = "Hard",
                ans1 = "interface Shape { getArea(); getPerimeter(); } class Rectangle implements Shape {...}",
                ans2 = "interface MyInterface {...}",
                ans3 = "class Rectangle extends Shape {...}",
                ans4 = "abstract class Shape {...}",
                number = 21,
                correctAnswerIndex = "interface Shape { getArea(); getPerimeter(); } class Rectangle implements Shape {...}"
                ,
                isIdentification = false// Answer is interface Shape { getArea(); getPerimeter(); } class Rectangle implements Shape {...}
            ),
            Trivia(
                question = "GCD of two numbers using Euclid's algorithm in C++.",
                category = "C++",
                difficulty = "Hard",
                ans1 = "int gcd(int a, int b) {...}",
                ans2 = "while(b != 0) { b--; }",
                ans3 = "for(a != 0 && b != 0)",
                ans4 = "a * b;",
                number = 22,
                correctAnswerIndex = "int gcd(int a, int b) {...}",
                isIdentification = false // Answer is int gcd(int a, int b) {...}
            ),
            Trivia(
                question = "Convert binary string to decimal in Java.",
                category = "Java",
                difficulty = "Hard",
                ans1 = "Integer.parseInt(binaryString, 2);",
                ans2 = "binaryString.toDecimal();",
                ans3 = "Double.parseDouble(binaryString);",
                ans4 = "binaryString.charAt(0);",
                number = 23,
                correctAnswerIndex = "Integer.parseInt(binaryString, 2);",
                isIdentification = false // Answer is Integer.parseInt(binaryString, 2);
            ),

            Trivia(
                question = "Check if string is palindrome in C++.",
                category = "C++",
                difficulty = "Hard",
                ans1 = "return s == string(s.rbegin(), s.rend());",
                ans2 = "reverse(s.begin(), s.end())",
                ans3 = "reverseString(s)",
                ans4 = "while(left < right)",
                number = 24,
                correctAnswerIndex = "return s == string(s.rbegin(), s.rend());",
                isIdentification = false
            ),

            Trivia(
                question = "Method overloading in Java.",
                category = "Java",
                difficulty = "Hard",
                ans1 = "void add(int a, int b) {...} void add(double a, b) {...}",
                ans2 = "super.add(int a, int b)",
                ans3 = "addNumbers(int a, int b)",
                ans4 = "this.add(int a, int b)",
                number = 25,
                correctAnswerIndex = "void add(int a, int b) {...} void add(double a, b) {...}",
                isIdentification = false
            ),

            Trivia(
                question = "Lambda expression to sort array in descending order (C++).",
                category = "C++",
                difficulty = "Hard",
                ans1 = "std::sort(arr.begin(), arr.end(), [](int a, int b) {...});",
                ans2 = "std::greater(arr.begin(),...)",
                ans3 = "reverse(arr.begin(), arr.end())",
                ans4 = "while(a < b)",
                number = 26,
                correctAnswerIndex = "std::sort(arr.begin(), arr.end(), [](int a, int b) {...});",
                isIdentification = false
            ),

            Trivia(
                question = "Inheritance: Car extends Vehicle in Java.",
                category = "Java",
                difficulty = "Hard",
                ans1 = "class Car extends Vehicle {...}",
                ans2 = "class Car implements Vehicle",
                ans3 = "Car super Vehicle {...}",
                ans4 = "Vehicle(Car car)",
                number = 27,
                correctAnswerIndex = "class Car extends Vehicle {...}",
                isIdentification = false

            ),

            Trivia(
                question = "Demonstrate virtual function in C++. ",
                category = "C++",
                difficulty = "Hard",
                ans1 = "virtual void print() {...}",
                ans2 = "virtual void BaseFunc() = 0;",
                ans3 = "void PrintBase()",
                ans4 = "cout << \"Base\";",
                number = 28,
                correctAnswerIndex = "virtual void print() {...}",
                isIdentification = false
            ),

            Trivia(
                question = "Read file line by line using BufferedReader in Java.",
                category = "Java",
                difficulty = "Hard",
                ans1 = "BufferedReader reader = new BufferedReader(...);",
                ans2 = "FileInputStream fis",
                ans3 = "read(file).lineByLine()",
                ans4 = "while(file.hasNextLine())",
                number = 29,
                correctAnswerIndex = "BufferedReader reader = new BufferedReader(...);",
                isIdentification = false
            ),

            Trivia(
                question = "Overload + operator to add complex numbers in C++.",
                category = "C++",
                difficulty = "Hard",
                ans1 = "Complex operator+(const Complex& c) {...}",
                ans2 = "Complex add(Complex& c) {...}",
                ans3 = "void operator+(Complex& c)",
                ans4 = "addComplex(Complex& c)",
                number = 30,
                correctAnswerIndex = "Complex operator+(const Complex& c) {...}",
                isIdentification = false
            ),
            Trivia(
                question = "Write a program to print 'Hello, World!' in C++.",
                category = "C++",
                difficulty = "Medium",
                ans1 = "std::cout << \"Hello, World!\";",
                ans2 = "printf(\"Hello, World!\");",
                ans3 = "cout << Hello << World;",
                ans4 = "std::print(\"Hello\");",
                number = 31,
                correctAnswerIndex = "std::cout << \"Hello, World!\";",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to print 'Hello, World!' in Java.",
                category = "Java",
                difficulty = "Medium",
                ans1 = "System.out.println(\"Hello, World!\");",
                ans2 = "System.print(\"Hello\");",
                ans3 = "println(\"Hello, World!\");",
                ans4 = "out.println(\"Hello\");",
                number = 32,
                correctAnswerIndex = "System.out.println(\"Hello, World!\");",
                isIdentification = false
            ),

            Trivia(
                question = "Write a function to find the sum of two numbers in C++.",
                category = "C++",
                difficulty = "Medium",
                ans1 = "int sum(int a, int b) { return a + b; }",
                ans2 = "a - b;",
                ans3 = "multiply(a, b);",
                ans4 = "for(a + b);",
                number = 33,
                correctAnswerIndex = "int sum(int a, int b) { return a + b; }",
                isIdentification = false
            ),

            Trivia(
                question = "Write a function to find the sum of two numbers in Java.",
                category = "Java",
                difficulty = "Medium",
                ans1 = "int sum(int a, int b) { return a + b; }",
                ans2 = "return a * b;",
                ans3 = "System.out.print(a - b);",
                ans4 = "sum(a, b) == a - b;",
                number = 34,
                correctAnswerIndex = "int sum(int a, int b) { return a + b; }",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to find the largest of two numbers in C++.",
                category = "C++",
                difficulty = "Medium",
                ans1 = "return (a > b) ? a : b;",
                ans2 = "return a + b;",
                ans3 = "max(a, b);",
                ans4 = "return a - b;",
                number = 35,
                correctAnswerIndex = "return (a > b) ? a : b;",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to find the largest of two numbers in Java.",
                category = "Java",
                difficulty = "Medium",
                ans1 = "return Math.max(a, b);",
                ans2 = "return a + b;",
                ans3 = "Math.min(a, b);",
                ans4 = "System.out.max(a, b);",
                number = 36,
                correctAnswerIndex = "return Math.max(a, b);",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to check if a number is even in C++.",
                category = "C++",
                difficulty = "Medium",
                ans1 = "return n % 2 == 0;",
                ans2 = "return n % 3 == 0;",
                ans3 = "return n / 2 == 0;",
                ans4 = "return n * 2 == 0;",
                number = 37,
                correctAnswerIndex = "return n % 2 == 0;",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to check if a number is even in Java.",
                category = "Java",
                difficulty = "Medium",
                ans1 = "return n % 2 == 0;",
                ans2 = "return n % 3 == 0;",
                ans3 = "if(n / 2 == 0)",
                ans4 = "if(n * 2 == 0)",
                number = 38,
                correctAnswerIndex = "return n % 2 == 0;",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to find the square of a number in C++.",
                category = "C++",
                difficulty = "Medium",
                ans1 = "return n * n;",
                ans2 = "return n / n;",
                ans3 = "squareRoot(n);",
                ans4 = "return n + n;",
                number = 39,
                correctAnswerIndex = "return n * n;",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to find the square of a number in Java.",
                category = "Java",
                difficulty = "Medium",
                ans1 = "return n * n;",
                ans2 = "Math.sqrt(n);",
                ans3 = "return n / n;",
                ans4 = "return n + n;",
                number = 40,
                correctAnswerIndex = "return n * n;",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to swap two numbers in C++.",
                category = "C++",
                difficulty = "Medium",
                ans1 = "swap(a, b);",
                ans2 = "a = b;",
                ans3 = "a + b = 0;",
                ans4 = "a * b;",
                number = 41,
                correctAnswerIndex = "swap(a, b);",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to swap two numbers in Java.",
                category = "Java",
                difficulty = "Medium",
                ans1 = "temp = a; a = b; b = temp;",
                ans2 = "Math.swap(a, b);",
                ans3 = "a = b;",
                ans4 = "a + b = 0;",
                number = 42,
                correctAnswerIndex = "temp = a; a = b; b = temp;",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to check if a number is positive in C++.",
                category = "C++",
                difficulty = "Medium",
                ans1 = "return n > 0;",
                ans2 = "return n < 0;",
                ans3 = "n == 0;",
                ans4 = "n * -1;",
                number = 43,
                correctAnswerIndex = "return n > 0;",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to check if a number is positive in Java.",
                category = "Java",
                difficulty = "Medium",
                ans1 = "return n > 0;",
                ans2 = "return n < 0;",
                ans3 = "if(n == 0)",
                ans4 = "n * -1;",
                number = 44,
                correctAnswerIndex = "return n > 0;",
                isIdentification = false
            ),

            Trivia(
                question = "Write a program to print the first 10 natural numbers in C++.",
                category = "C++",
                difficulty = "Medium",
                ans1 = "for(int i=1; i<=10; i++) cout << i;",
                ans2 = "while(i<10)",
                ans3 = "do{i++}",
                ans4 = "i += 10;",
                number = 45,
                correctAnswerIndex = "for(int i=1; i<=10; i++) cout << i;",
                isIdentification = false
            ),
            Trivia(
                question = "What is the size of int data type in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "16-bit",
                ans2 = "32-bit",
                ans3 = "64-bit",
                ans4 = "128-bit",
                number = 46,
                correctAnswerIndex = "32-bit",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is not a Java keyword?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "final",
                ans2 = "static",
                ans3 = "String",
                ans4 = "class",
                number = 47,
                correctAnswerIndex = "String",
                isIdentification = false
            ),

            Trivia(
                question = "What is the default value of a boolean in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "true",
                ans2 = "false",
                ans3 = "0",
                ans4 = "null",
                number = 48,
                correctAnswerIndex = "false",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is used to create an object in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "import",
                ans2 = "package",
                ans3 = "new",
                ans4 = "instanceof",
                number = 49,
                correctAnswerIndex = "new",
                isIdentification = false
            ),

            Trivia(
                question = "What is the default value of an uninitialized char variable?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "'\\u0000'",
                ans2 = "0",
                ans3 = "null",
                ans4 = "undefined",
                number = 50,
                correctAnswerIndex = "'\\u0000'",
                isIdentification = false
            ),

            Trivia(
                question = "Which keyword is used to inherit a class in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "inherits",
                ans2 = "extends",
                ans3 = "implements",
                ans4 = "super",
                number = 6,
                correctAnswerIndex = "extends",
                isIdentification = false
            ),

            Trivia(
                question = "Which method must be implemented by all Java threads?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "start()",
                ans2 = "run()",
                ans3 = "main()",
                ans4 = "init()",
                number = 7,
                correctAnswerIndex = "run()",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is a valid declaration of a float variable?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "float f = 1.0;",
                ans2 = "float f = 1.0f;",
                ans3 = "float f = \"1.0\";",
                ans4 = "float f = 1;",
                number = 8,
                correctAnswerIndex = "float f = 1.0f;",
                isIdentification = false
            ),

            Trivia(
                question = "Which operator is used to concatenate two strings in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "+",
                ans2 = "*",
                ans3 = "&",
                ans4 = "%",
                number = 9,
                correctAnswerIndex = "+",
                isIdentification = false
            ),

            Trivia(
                question = "Which of these data types does not hold numeric values?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "int",
                ans2 = "float",
                ans3 = "char",
                ans4 = "double",
                number = 10,
                correctAnswerIndex = "char",
                isIdentification = false
            ),
            Trivia(
                question = "What does the final keyword mean when applied to a variable in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "The variable can be reassigned.",
                ans2 = "The variable cannot be reassigned.",
                ans3 = "The variable can only be assigned null values.",
                ans4 = "The variable is removed from memory after initialization.",
                number = 11,
                correctAnswerIndex = "The variable cannot be reassigned.",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following loops in Java will always execute at least once?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "for",
                ans2 = "while",
                ans3 = "do-while",
                ans4 = "foreach",
                number = 12,
                correctAnswerIndex = "do-while",
                isIdentification = false
            ),
            Trivia(
                question = "What is the result of 5 % 2 in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "2",
                ans2 = "0",
                ans3 = "1",
                ans4 = "5",
                number = 13,
                correctAnswerIndex = "1",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is a checked exception in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "NullPointerException",
                ans2 = "ArrayIndexOutOfBoundsException",
                ans3 = "IOException",
                ans4 = "ArithmeticException",
                number = 14,
                correctAnswerIndex = "IOException",
                isIdentification = false
            ),
            Trivia(
                question = "What is the output of System.out.println(3 + 4 + 'Java');?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "7Java",
                ans2 = "34Java",
                ans3 = "Java34",
                ans4 = "3 + 4Java",
                number = 15,
                correctAnswerIndex = "7Java",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is not a primitive data type in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "int",
                ans2 = "float",
                ans3 = "boolean",
                ans4 = "String",
                number = 16,
                correctAnswerIndex = "String",
                isIdentification = false
            ),
            Trivia(
                question = "What is the use of super keyword in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "To refer to the current class instance",
                ans2 = "To refer to the base class instance",
                ans3 = "To refer to a static variable",
                ans4 = "To create an object of a class",
                number = 17,
                correctAnswerIndex = "To refer to the base class instance",
                isIdentification = false
            ),
            Trivia(
                question = "What is the term for the process of converting a primitive data type into an object in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "Serialization",
                ans2 = "Unboxing",
                ans3 = "Autoboxing",
                ans4 = "Typecasting",
                number = 18,
                correctAnswerIndex = "Autoboxing",
                isIdentification = false
            ),
            Trivia(
                question = "What will be the result of the following expression: true && false?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "true",
                ans2 = "false",
                ans3 = "null",
                ans4 = "1",
                number = 19,
                correctAnswerIndex = "false",
                isIdentification = false
            ),
            Trivia(
                question = "Which package is imported by default in every Java program?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "java.util",
                ans2 = "java.net",
                ans3 = "java.io",
                ans4 = "java.lang",
                number = 20,
                correctAnswerIndex = "java.lang",
                isIdentification = false
            ),
            Trivia(
                question = "Which access modifier makes a member accessible within its own package and by subclasses?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "private",
                ans2 = "public",
                ans3 = "protected",
                ans4 = "final",
                number = 21,
                correctAnswerIndex = "protected",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is the correct syntax to declare a two-dimensional array in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "int[][] arr = new int[3][3];",
                ans2 = "int arr = new int[3, 3];",
                ans3 = "int[3][3] arr = new int[][];",
                ans4 = "int[] arr = new int[3][3];",
                number = 22,
                correctAnswerIndex = "int[][] arr = new int[3][3];",
                isIdentification = false
            ),
            Trivia(
                question = "What is the output of System.out.println('Java'.charAt(2));?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "J",
                ans2 = "v",
                ans3 = "a",
                ans4 = "a\\0",
                number = 23,
                correctAnswerIndex = "v",
                isIdentification = false
            ),
            Trivia(
                question = "Which class is the superclass of all classes in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Object",
                ans2 = "Main",
                ans3 = "String",
                ans4 = "System",
                number = 24,
                correctAnswerIndex = "Object",
                isIdentification = false
            ),
            Trivia(
                question = "What is the output of System.out.println(10 > 9 ? 'True' : 'False');?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "True",
                ans2 = "False",
                ans3 = "Error",
                ans4 = "10 > 9",
                number = 25,
                correctAnswerIndex = "True",
                isIdentification = false
            ),
            Trivia(
                question = "Which statement is true about constructors in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "A class can have multiple constructors with the same parameter list.",
                ans2 = "A constructor can have a return type.",
                ans3 = "Constructors are called when an object is created.",
                ans4 = "The constructor’s name is always initialize.",
                number = 26,
                correctAnswerIndex = "Constructors are called when an object is created.",
                isIdentification = false
            ),
            Trivia(
                question = "Which keyword is used to declare an abstract class in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "abstract",
                ans2 = "final",
                ans3 = "static",
                ans4 = "extends",
                number = 27,
                correctAnswerIndex = "abstract",
                isIdentification = false
            ),
            Trivia(
                question = "Which method is used to compare two strings in Java for equality?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "equals()",
                ans2 = "==",
                ans3 = "compare()",
                ans4 = "match()",
                number = 28,
                correctAnswerIndex = "equals()",
                isIdentification = false
            ),
            Trivia(
                question = "What will be the output of the following statement: System.out.println(Math.max(10, 20));?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "10",
                ans2 = "20",
                ans3 = "Math error",
                ans4 = "0",
                number = 29,
                correctAnswerIndex = "20",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is the correct extension of C++ files?",
                category = "C++",
                difficulty = "Easy",
                ans1 = ".cpp",
                ans2 = ".cpl",
                ans3 = ".cls",
                ans4 = ".c++",
                number = 1,
                correctAnswerIndex = ".cpp",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is the correct syntax to start the main() function in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "void main()",
                ans2 = "int main()",
                ans3 = "float main()",
                ans4 = "main()",
                number = 2,
                correctAnswerIndex = "int main()",
                isIdentification = false
            ),
            Trivia(
                question = "Which operator is used for output in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = ">>",
                ans2 = "<<",
                ans3 = "=",
                ans4 = "**",
                number = 3,
                correctAnswerIndex = "<<",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following symbols is used to end a statement in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = ".",
                ans2 = ":",
                ans3 = ";",
                ans4 = "!",
                number = 4,
                correctAnswerIndex = ";",
                isIdentification = false
            ),
            Trivia(
                question = "How do you write a single-line comment in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "# Comment",
                ans2 = "// Comment",
                ans3 = "/* Comment */",
                ans4 = "<!-- Comment -->",
                number = 5,
                correctAnswerIndex = "// Comment",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following data types is used to store floating-point numbers in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "int",
                ans2 = "float",
                ans3 = "char",
                ans4 = "bool",
                number = 6,
                correctAnswerIndex = "float",
                isIdentification = false
            ),
            Trivia(
                question = "What is the value of true in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "1",
                ans2 = "0",
                ans3 = "-1",
                ans4 = "2",
                number = 7,
                correctAnswerIndex = "1",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following loops executes at least once, even if the condition is false?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "for loop",
                ans2 = "while loop",
                ans3 = "do-while loop",
                ans4 = "if statement",
                number = 8,
                correctAnswerIndex = "do-while loop",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is the correct way to declare a variable in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "int 5number;",
                ans2 = "int number5;",
                ans3 = "int number 5;",
                ans4 = "int number := 5;",
                number = 9,
                correctAnswerIndex = "int number5;",
                isIdentification = false
            ),
            Trivia(
                question = "Which header file is required to use cout and cin in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "#include <stdio.h>",
                ans2 = "#include <conio.h>",
                ans3 = "#include <iostream>",
                ans4 = "#include <math.h>",
                number = 10,
                correctAnswerIndex = "#include <iostream>",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is a valid C++ data type?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "string",
                ans2 = "float",
                ans3 = "real",
                ans4 = "text",
                number = 11,
                correctAnswerIndex = "float",
                isIdentification = false
            ),
            Trivia(
                question = "What will the following statement output? cout << 'Hello World';",
                category = "C++",
                difficulty = "Easy",
                ans1 = "Hello World",
                ans2 = "hello world",
                ans3 = "HELLO WORLD",
                ans4 = "Error",
                number = 12,
                correctAnswerIndex = "Hello World",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is a relational operator in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "+",
                ans2 = "=",
                ans3 = "<=",
                ans4 = "&&",
                number = 13,
                correctAnswerIndex = "<=",
                isIdentification = false
            ),
            Trivia(
                question = "How do you declare a constant variable in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "int const x = 5;",
                ans2 = "const int x = 5;",
                ans3 = "Both a and b",
                ans4 = "constant int x = 5;",
                number = 14,
                correctAnswerIndex = "Both a and b",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is the correct syntax to create a function in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "void functionName {}",
                ans2 = "function void functionName() {}",
                ans3 = "void functionName() {}",
                ans4 = "function void() {}",
                number = 15,
                correctAnswerIndex = "void functionName() {}",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following loops is used when the number of iterations is known beforehand?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "while loop",
                ans2 = "do-while loop",
                ans3 = "for loop",
                ans4 = "if statement",
                number = 16,
                correctAnswerIndex = "for loop",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following statements is used to take input from the user in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "input",
                ans2 = "cin",
                ans3 = "scanf",
                ans4 = "get()",
                number = 17,
                correctAnswerIndex = "cin",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is used to allocate memory dynamically in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "malloc",
                ans2 = "new",
                ans3 = "create",
                ans4 = "allocate",
                number = 18,
                correctAnswerIndex = "new",
                isIdentification = false
            ),
            Trivia(
                question = "What will be the output of the following code? int x = 10; cout << x++;",
                category = "C++",
                difficulty = "Medium",
                ans1 = "9",
                ans2 = "10",
                ans3 = "11",
                ans4 = "Error",
                number = 19,
                correctAnswerIndex = "10",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is a logical operator in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "+",
                ans2 = "==",
                ans3 = "&&",
                ans4 = "!",
                number = 20,
                correctAnswerIndex = "&&",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is used to define a block of code in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "() (parentheses)",
                ans2 = "{} (curly braces)",
                ans3 = "<> (angle brackets)",
                ans4 = "[] (square brackets)",
                number = 21,
                correctAnswerIndex = "{} (curly braces)",
                isIdentification = false
            ),

            Trivia(
                question = "What is the result of 5 % 2 in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "1",
                ans2 = "2",
                ans3 = "0",
                ans4 = "5",
                number = 22,
                correctAnswerIndex = "1",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is used to break out of a loop in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "stop",
                ans2 = "exit",
                ans3 = "break",
                ans4 = "end",
                number = 23,
                correctAnswerIndex = "break",
                isIdentification = false
            ),

            Trivia(
                question = "Which keyword is used to return a value from a function in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "send",
                ans2 = "return",
                ans3 = "output",
                ans4 = "give",
                number = 24,
                correctAnswerIndex = "return",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is not a valid C++ keyword?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "if",
                ans2 = "then",
                ans3 = "else",
                ans4 = "while",
                number = 25,
                correctAnswerIndex = "then",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following data types is used to store a single character in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "int",
                ans2 = "char",
                ans3 = "bool",
                ans4 = "float",
                number = 26,
                correctAnswerIndex = "char",
                isIdentification = false
            ),

            Trivia(
                question = "What will be the output of the following code? int x = 10; int y = 20; cout << x + y;",
                category = "C++",
                difficulty = "Medium",
                ans1 = "1020",
                ans2 = "10 20",
                ans3 = "30",
                ans4 = "Error",
                number = 27,
                correctAnswerIndex = "30",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is not a C++ data type?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "int",
                ans2 = "float",
                ans3 = "string",
                ans4 = "real",
                number = 28,
                correctAnswerIndex = "real",
                isIdentification = false
            ),

            Trivia(
                question = "Which loop will continue executing while the condition is true?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "if",
                ans2 = "switch",
                ans3 = "for",
                ans4 = "case",
                number = 29,
                correctAnswerIndex = "for",
                isIdentification = false
            ),

            Trivia(
                question = "What does the continue statement do in a loop?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "Exits the loop",
                ans2 = "Skips the current iteration and moves to the next",
                ans3 = "Stops the program",
                ans4 = "Restarts the loop",
                number = 30,
                correctAnswerIndex = "Skips the current iteration and moves to the next",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is the correct way to declare an integer variable in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "int x;",
                ans2 = "integer x;",
                ans3 = "x = int;",
                ans4 = "var x: int;",
                number = 31,
                correctAnswerIndex = "int x;",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is a valid loop in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "loop (i = 0; i < 5; i++)",
                ans2 = "for (int i = 0; i < 5; i++)",
                ans3 = "while (int i < 5)",
                ans4 = "repeat i < 5",
                number = 32,
                correctAnswerIndex = "for (int i = 0; i < 5; i++)",
                isIdentification = false
            ),

            Trivia(
                question = "Which data type is used to store a character in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "int",
                ans2 = "char",
                ans3 = "float",
                ans4 = "string",
                number = 33,
                correctAnswerIndex = "char",
                isIdentification = false
            ),

            Trivia(
                question = "Which keyword is used to define a function in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "function",
                ans2 = "def",
                ans3 = "void",
                ans4 = "method",
                number = 34,
                correctAnswerIndex = "void",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is used for single-line comments in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "//",
                ans2 = "/* */",
                ans3 = "#",
                ans4 = "<!-- -->",
                number = 35,
                correctAnswerIndex = "//",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is the correct way to define a function in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "def functionName() {}",
                ans2 = "function functionName() {}",
                ans3 = "void functionName() {}",
                ans4 = "func functionName() {}",
                number = 36,
                correctAnswerIndex = "void functionName() {}",
                isIdentification = false
            ),

            Trivia(
                question = "What will the following C++ code output?\n#include <iostream>\nusing namespace std;\n\nint main() {\n    cout << \"Hello, World!\";\n    return 0;\n}",
                category = "C++",
                difficulty = "Easy",
                ans1 = "Hello",
                ans2 = "Hello, World",
                ans3 = "Hello, World!",
                ans4 = "Error",
                number = 37,
                correctAnswerIndex = "Hello, World!",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is used to take input from the user in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "cin",
                ans2 = "in",
                ans3 = "input",
                ans4 = "read",
                number = 38,
                correctAnswerIndex = "cin",
                isIdentification = false
            ),

            Trivia(
                question = "What is the correct way to declare a constant integer in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "const int x = 5;",
                ans2 = "int const x = 5;",
                ans3 = "constant int x = 5;",
                ans4 = "Both a and b",
                number = 39,
                correctAnswerIndex = "Both a and b",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct syntax for a while loop in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "while (condition) {}",
                ans2 = "do { condition } while;",
                ans3 = "while (condition) then {}",
                ans4 = "loop while condition {}",
                number = 40,
                correctAnswerIndex = "while (condition) {}",
                isIdentification = false
            ),

            Trivia(
                question = "What is the correct syntax for a conditional statement in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "if x > 5 then {}",
                ans2 = "if (x > 5) {}",
                ans3 = "if x > 5; {}",
                ans4 = "if (x > 5) then {}",
                number = 41,
                correctAnswerIndex = "if (x > 5) {}",
                isIdentification = false
            ),

            Trivia(
                question = "What is the size of a char data type in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "1 byte",
                ans2 = "2 bytes",
                ans3 = "4 bytes",
                ans4 = "8 bytes",
                number = 42,
                correctAnswerIndex = "1 byte",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is the correct way to declare a float variable in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "float x;",
                ans2 = "real x;",
                ans3 = "decimal x;",
                ans4 = "f x;",
                number = 43,
                correctAnswerIndex = "float x;",
                isIdentification = false
            ),

            Trivia(
                question = "What will the following code print?\n#include <iostream>\nusing namespace std;\n\nint main() {\n    int x = 5;\n    int y = 10;\n    cout << x + y;\n    return 0;\n}",
                category = "C++",
                difficulty = "Easy",
                ans1 = "5",
                ans2 = "10",
                ans3 = "15",
                ans4 = "Error",
                number = 44,
                correctAnswerIndex = "15",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following statements about arrays is true in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "Arrays are dynamically sized.",
                ans2 = "Arrays in C++ can hold only primitive data types.",
                ans3 = "Arrays in C++ are fixed in size.",
                ans4 = "Arrays are created using the new keyword.",
                number = 45,
                correctAnswerIndex = "Arrays in C++ are fixed in size.",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following operators is used for equality comparison in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "=",
                ans2 = "==",
                ans3 = "!=",
                ans4 = "=>",
                number = 46,
                correctAnswerIndex = "==",
                isIdentification = false
            ),

            Trivia(
                question = "What will the following C++ code output?\n#include <iostream>\nusing namespace std;\n\nint main() {\n    int x = 4;\n    cout << x * 2;\n    return 0;\n}",
                category = "C++",
                difficulty = "Easy",
                ans1 = "2",
                ans2 = "4",
                ans3 = "8",
                ans4 = "16",
                number = 47,
                correctAnswerIndex = "8",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct way to write a comment in C++ that spans multiple lines?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "/* This is a comment */",
                ans2 = "// This is a comment",
                ans3 = "/* This is a comment",
                ans4 = "# This is a comment",
                number = 48,
                correctAnswerIndex = "/* This is a comment */",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is used to allocate memory dynamically for an array in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "new[]",
                ans2 = "malloc()",
                ans3 = "alloc()",
                ans4 = "dynamic[]",
                number = 49,
                correctAnswerIndex = "new[]",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is used to deallocate memory dynamically allocated with new in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "delete[]",
                ans2 = "dealloc()",
                ans3 = "free()",
                ans4 = "dispose()",
                number = 50,
                correctAnswerIndex = "delete[]",
                isIdentification = false
            ),

            Trivia(
                question = "What is the correct syntax for using a switch statement in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "switch x { case 1: ... }",
                ans2 = "switch (x) { case 1: ... }",
                ans3 = "switch { case x: ... }",
                ans4 = "switch (x) { 1: ... }",
                number = 51,
                correctAnswerIndex = "switch (x) { case 1: ... }",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is used to define a constant in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "const",
                ans2 = "final",
                ans3 = "constant",
                ans4 = "static",
                number = 52,
                correctAnswerIndex = "const",
                isIdentification = false
            ),

            Trivia(
                question = "What will the following C++ code output?\n#include <iostream>\nusing namespace std;\n\nint main() {\n    cout << \"C++ is fun!\" << endl;\n    return 0;\n}",
                category = "C++",
                difficulty = "Easy",
                ans1 = "C++ is fun!",
                ans2 = "C++ is fun! followed by a new line",
                ans3 = "C++ is fun",
                ans4 = "Error",
                number = 53,
                correctAnswerIndex = "C++ is fun! followed by a new line",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is true about a for loop in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "The loop condition must always be true.",
                ans2 = "A for loop must have an increment/decrement statement.",
                ans3 = "The loop must run at least once.",
                ans4 = "A for loop has three parts: initialization, condition, and update.",
                number = 54,
                correctAnswerIndex = "A for loop has three parts: initialization, condition, and update.",
                isIdentification = false
            ),

            Trivia(
                question = "Which operator is used to access members of a structure in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = ".",
                ans2 = "->",
                ans3 = "&",
                ans4 = "*",
                number = 55,
                correctAnswerIndex = ".",
                isIdentification = false
            ),

            Trivia(
                question = "What will the following C++ code output?\n#include <iostream>\nusing namespace std;\n\nint main() {\n    bool flag = true;\n    cout << flag;\n    return 0;\n}",
                category = "C++",
                difficulty = "Easy",
                ans1 = "1",
                ans2 = "true",
                ans3 = "0",
                ans4 = "false",
                number = 56,
                correctAnswerIndex = "1",
                isIdentification = false
            ),Trivia(
                question = "Which of the following is the correct syntax for a do-while loop in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "do { ... } while (condition);",
                ans2 = "do while (condition) { ... }",
                ans3 = "do { ... } until (condition);",
                ans4 = "while do { ... }",
                number = 57,
                correctAnswerIndex = "do { ... } while (condition);",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct syntax for a for loop in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "for (int i = 0; i < 10; i++) {}",
                ans2 = "for (i = 0; i < 10) {}",
                ans3 = "for (int i; i < 10; i++) {}",
                ans4 = "for (i = 0; i < 10; i--) {}",
                number = 58,
                correctAnswerIndex = "for (int i = 0; i < 10; i++) {}",
                isIdentification = false
            ),

            Trivia(
                question = "What is the default value of an uninitialized int variable in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "0",
                ans2 = "Undefined",
                ans3 = "-1",
                ans4 = "Garbage value",
                number = 59,
                correctAnswerIndex = "Undefined",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is true for functions in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "Functions must return a value.",
                ans2 = "Functions can return no value using the void return type.",
                ans3 = "Functions cannot take parameters.",
                ans4 = "Functions cannot be overloaded.",
                number = 60,
                correctAnswerIndex = "Functions can return no value using the void return type.",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following operators is used to perform division in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "/",
                ans2 = "//",
                ans3 = "%",
                ans4 = "\\",
                number = 61,
                correctAnswerIndex = "/",
                isIdentification = false
            ),

            Trivia(
                question = "What does the following C++ code do?\nint x = 3;\nint y = x++;\ncout << y;",
                category = "C++",
                difficulty = "Easy",
                ans1 = "Prints 3",
                ans2 = "Prints 4",
                ans3 = "Prints 2",
                ans4 = "Causes an error",
                number = 62,
                correctAnswerIndex = "Prints 3",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct syntax for declaring a pointer to an integer in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "int ptr*;",
                ans2 = "int *ptr;",
                ans3 = "pointer int ptr;",
                ans4 = "int& ptr;",
                number = 63,
                correctAnswerIndex = "int *ptr;",
                isIdentification = false
            ),

            Trivia(
                question = "What does the following C++ code output?\nint x = 5;\nx = x * 2;\ncout << x;",
                category = "C++",
                difficulty = "Medium",
                ans1 = "5",
                ans2 = "10",
                ans3 = "15",
                ans4 = "20",
                number = 64,
                correctAnswerIndex = "10",
                isIdentification = false
            ),

            Trivia(
                question = "What will be the output of the following code?\n#include <iostream>\nusing namespace std;\n\nint main() {\n    int x = 10, y = 20;\n    cout << (x == y);\n    return 0;\n}",
                category = "C++",
                difficulty = "Medium",
                ans1 = "0",
                ans2 = "1",
                ans3 = "10",
                ans4 = "20",
                number = 65,
                correctAnswerIndex = "0",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following functions is used to find the absolute value of a number in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "abs()",
                ans2 = "absolute()",
                ans3 = "fabs()",
                ans4 = "fabs() and abs()",
                number = 66,
                correctAnswerIndex = "abs()",
                isIdentification = false
            ),

            Trivia(
                question = "What is the size of a long data type in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "2 bytes",
                ans2 = "4 bytes",
                ans3 = "8 bytes",
                ans4 = "Depends on the system",
                number = 67,
                correctAnswerIndex = "Depends on the system",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is used to find the remainder of a division in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "div()",
                ans2 = "mod()",
                ans3 = "%",
                ans4 = "remainder()",
                number = 68,
                correctAnswerIndex = "%",
                isIdentification = false
            ),

            Trivia(
                question = "What will be the output of the following C++ code?\n#include <iostream>\nusing namespace std;\n\nint main() {\n    int x = 5;\n    int y = 3;\n    cout << x / y;\n    return 0;\n}",
                category = "C++",
                difficulty = "Medium",
                ans1 = "1.6667",
                ans2 = "1",
                ans3 = "2",
                ans4 = "5",
                number = 69,
                correctAnswerIndex = "1",
                isIdentification = false
            ),

            Trivia(
                question = "What is the result of the following C++ expression?\nint x = 7;\ncout << x % 3;",
                category = "C++",
                difficulty = "Easy",
                ans1 = "0",
                ans2 = "1",
                ans3 = "2",
                ans4 = "3",
                number = 70,
                correctAnswerIndex = "2",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is a correct statement to declare a string literal in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "string s = 'Hello';",
                ans2 = "string s = \"Hello\";",
                ans3 = "string s = Hello;",
                ans4 = "string s = \"H\";",
                number = 71,
                correctAnswerIndex = "string s = \"Hello\";",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct syntax to call a member function display() of a class Car in C++?",
                category = "C++",
                difficulty = "Easy",
                ans1 = "Car.display();",
                ans2 = "display.Car();",
                ans3 = "Car::display();",
                ans4 = "display();",
                number = 72,
                correctAnswerIndex = "Car.display();",
                isIdentification = false
            ),

            Trivia(
                question = "What is the default access modifier for class members in C++?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "private",
                ans2 = "public",
                ans3 = "protected",
                ans4 = "None",
                number = 73,
                correctAnswerIndex = "private",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following C++ operators is used to allocate memory dynamically for a single object?",
                category = "C++",
                difficulty = "Medium",
                ans1 = "new[]",
                ans2 = "malloc()",
                ans3 = "new",
                ans4 = "malloc() and new",
                number = 74,
                correctAnswerIndex = "new",
                isIdentification = false
            ),
            Trivia(
                question = "What is the correct way to declare a variable of type int in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "int x;",
                ans2 = "integer x;",
                ans3 = "float x;",
                ans4 = "num x;",
                number = 1,
                correctAnswerIndex = "int x;",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct way to print \"Hello, Java!\" in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "System.out.print(\"Hello, Java!\");",
                ans2 = "System.print(\"Hello, Java!\");",
                ans3 = "console.print(\"Hello, Java!\");",
                ans4 = "echo(\"Hello, Java!\");",
                number = 2,
                correctAnswerIndex = "System.out.print(\"Hello, Java!\");",
                isIdentification = false
            ),

            Trivia(
                question = "Which keyword is used to define a class in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "class",
                ans2 = "def",
                ans3 = "object",
                ans4 = "function",
                number = 3,
                correctAnswerIndex = "class",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is used to create a new instance of a class in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "new",
                ans2 = "create",
                ans3 = "initialize",
                ans4 = "object",
                number = 4,
                correctAnswerIndex = "new",
                isIdentification = false
            ),

            Trivia(
                question = "What is the size of a char data type in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "1 byte",
                ans2 = "2 bytes",
                ans3 = "4 bytes",
                ans4 = "8 bytes",
                number = 5,
                correctAnswerIndex = "2 bytes",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct syntax for a for loop in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "for (int i = 0; i < 5; i++) {}",
                ans2 = "for int i = 0; i < 5; i++ {}",
                ans3 = "for i = 0; i < 5; i++ {}",
                ans4 = "for (i = 0, i < 5, i++) {}",
                number = 6,
                correctAnswerIndex = "for (int i = 0; i < 5; i++) {}",
                isIdentification = false
            ),

            Trivia(
                question = "Which method is used to get the length of a string in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "length()",
                ans2 = "size()",
                ans3 = "count()",
                ans4 = "lengthOf()",
                number = 7,
                correctAnswerIndex = "length()",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct way to define a method in Java that returns an integer?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "int methodName() {}",
                ans2 = "return int methodName() {}",
                ans3 = "method int methodName() {}",
                ans4 = "int methodName: {}",
                number = 8,
                correctAnswerIndex = "int methodName() {}",
                isIdentification = false
            ),

            Trivia(
                question = "What is the default value of an int variable in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "0",
                ans2 = "1",
                ans3 = "null",
                ans4 = "Undefined",
                number = 9,
                correctAnswerIndex = "0",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct syntax to call the main method in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "main(String[] args)",
                ans2 = "public static void main(String[] args)",
                ans3 = "public void main(String[] args)",
                ans4 = "void main(String[] args)",
                number = 10,
                correctAnswerIndex = "public static void main(String[] args)",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is used to take input from the user in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Scanner",
                ans2 = "InputStream",
                ans3 = "System.in",
                ans4 = "console",
                number = 11,
                correctAnswerIndex = "Scanner",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is true about Java arrays?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "Arrays in Java are dynamically sized.",
                ans2 = "Arrays in Java have a fixed size once initialized.",
                ans3 = "Arrays cannot store primitive data types.",
                ans4 = "Arrays must be initialized with new only.",
                number = 12,
                correctAnswerIndex = "Arrays in Java have a fixed size once initialized.",
                isIdentification = false
            ),

            Trivia(
                question = "What will the following code print?\nint x = 5;\nSystem.out.println(x++);",
                category = "Java",
                difficulty = "Medium",
                ans1 = "4",
                ans2 = "5",
                ans3 = "6",
                ans4 = "Error",
                number = 13,
                correctAnswerIndex = "5",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is a valid declaration of a constant in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "final int x = 10;",
                ans2 = "constant int x = 10;",
                ans3 = "int const x = 10;",
                ans4 = "int x = final 10;",
                number = 14,
                correctAnswerIndex = "final int x = 10;",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct way to define a class constructor in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "ClassName() {}",
                ans2 = "void ClassName() {}",
                ans3 = "ClassName() : {}",
                ans4 = "constructor ClassName() {}",
                number = 15,
                correctAnswerIndex = "ClassName() {}",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is used to terminate a loop in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "stop",
                ans2 = "exit",
                ans3 = "break",
                ans4 = "continue",
                number = 16,
                correctAnswerIndex = "break",
                isIdentification = false
            ),

            Trivia(
                question = "Which operator is used for equality comparison in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "=",
                ans2 = "==",
                ans3 = "!=",
                ans4 = "===",
                number = 17,
                correctAnswerIndex = "==",
                isIdentification = false
            ),

            Trivia(
                question = "What is the output of the following code?\nint x = 10, y = 3;\nSystem.out.println(x / y);",
                category = "Java",
                difficulty = "Medium",
                ans1 = "3",
                ans2 = "3.33",
                ans3 = "3.0",
                ans4 = "0",
                number = 18,
                correctAnswerIndex = "3",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is used to convert a string to an integer in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "intValue()",
                ans2 = "parseInt()",
                ans3 = "toInteger()",
                ans4 = "stringToInt()",
                number = 19,
                correctAnswerIndex = "parseInt()",
                isIdentification = false
            ),

            Trivia(
                question = "What is the correct syntax for declaring a method that does not return anything in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "void methodName() {}",
                ans2 = "method void methodName() {}",
                ans3 = "void: methodName() {}",
                ans4 = "noReturn methodName() {}",
                number = 20,
                correctAnswerIndex = "void methodName() {}",
                isIdentification = false
            ),
            Trivia(
                question = "Which of the following is used to declare a method in Java that takes no arguments and returns no value?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "void methodName() {}",
                ans2 = "method void methodName() {}",
                ans3 = "void: methodName() {}",
                ans4 = "noReturn methodName() {}",
                number = 21,
                correctAnswerIndex = "void methodName() {}",
                isIdentification = false
            ),

            Trivia(
                question = "What is the default value of an uninitialized boolean variable in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "false",
                ans2 = "true",
                ans3 = "null",
                ans4 = "0",
                number = 22,
                correctAnswerIndex = "false",
                isIdentification = false
            ),

            Trivia(
                question = "Which method is used to compare two strings lexicographically in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "equals()",
                ans2 = "compareTo()",
                ans3 = "compare()",
                ans4 = "isEqual()",
                number = 23,
                correctAnswerIndex = "compareTo()",
                isIdentification = false
            ),

            Trivia(
                question = "What is the correct syntax to create an array of integers in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "int[] arr = new int[5];",
                ans2 = "int arr[5] = new int[];",
                ans3 = "array int arr = new int(5);",
                ans4 = "int[] arr = int[5];",
                number = 24,
                correctAnswerIndex = "int[] arr = new int[5];",
                isIdentification = false
            ),

            Trivia(
                question = "What will the following code output?\nint a = 10, b = 20;\nSystem.out.println(a > b);",
                category = "Java",
                difficulty = "Medium",
                ans1 = "true",
                ans2 = "false",
                ans3 = "0",
                ans4 = "1",
                number = 25,
                correctAnswerIndex = "false",
                isIdentification = false
            ),

            Trivia(
                question = "What does the continue keyword do in a loop in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "It terminates the loop.",
                ans2 = "It restarts the loop from the beginning.",
                ans3 = "It skips the current iteration and proceeds to the next iteration.",
                ans4 = "It exits the program.",
                number = 26,
                correctAnswerIndex = "It skips the current iteration and proceeds to the next iteration.",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct way to create a String object in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "String s = \"Hello\";",
                ans2 = "String s = new String(\"Hello\");",
                ans3 = "String s = String(\"Hello\");",
                ans4 = "Both a and b",
                number = 27,
                correctAnswerIndex = "Both a and b",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is NOT a valid data type in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "int",
                ans2 = "float",
                ans3 = "char",
                ans4 = "number",
                number = 28,
                correctAnswerIndex = "number",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct way to declare a constant variable in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "final int x = 5;",
                ans2 = "const int x = 5;",
                ans3 = "int const x = 5;",
                ans4 = "constant int x = 5;",
                number = 29,
                correctAnswerIndex = "final int x = 5;",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct way to declare an array of strings in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "String[] arr = {\"apple\", \"banana\"};",
                ans2 = "String arr[] = {\"apple\", \"banana\"};",
                ans3 = "String arr = [\"apple\", \"banana\"];",
                ans4 = "Both a and b",
                number = 30,
                correctAnswerIndex = "Both a and b",
                isIdentification = false
            ),

            Trivia(
                question = "What is the output of the following code?\nint x = 5;\nSystem.out.println(x * 2);",
                category = "Java",
                difficulty = "Easy",
                ans1 = "10",
                ans2 = "50",
                ans3 = "25",
                ans4 = "x * 2",
                number = 31,
                correctAnswerIndex = "10",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct syntax for a while loop in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "while (x < 10) {}",
                ans2 = "while x < 10 {}",
                ans3 = "while (x < 10)",
                ans4 = "while {x < 10}",
                number = 32,
                correctAnswerIndex = "while (x < 10) {}",
                isIdentification = false
            ),

            Trivia(
                question = "What will be the result of the following code?\nint a = 5, b = 2;\nSystem.out.println(a % b);",
                category = "Java",
                difficulty = "Medium",
                ans1 = "2",
                ans2 = "1",
                ans3 = "5",
                ans4 = "0",
                number = 33,
                correctAnswerIndex = "1",
                isIdentification = false
            ),

            Trivia(
                question = "What is the default value of a String variable in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "\"null\"",
                ans2 = "null",
                ans3 = "\"\"",
                ans4 = "undefined",
                number = 34,
                correctAnswerIndex = "null",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is the correct syntax to create a new Scanner object in Java?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Scanner sc = new Scanner();",
                ans2 = "Scanner sc = new Scanner(System.in);",
                ans3 = "Scanner sc = new InputStreamReader();",
                ans4 = "Scanner sc = new Scanner(System.out);",
                number = 35,
                correctAnswerIndex = "Scanner sc = new Scanner(System.in);",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following Java keywords is used to create a new instance of a class?",
                category = "Java",
                difficulty = "Easy",
                ans1 = "new",
                ans2 = "create",
                ans3 = "object",
                ans4 = "instance",
                number = 36,
                correctAnswerIndex = "new",
                isIdentification = false
            ),

            Trivia(
                question = "What does the default keyword define in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "A value for an uninitialized variable.",
                ans2 = "The default behavior of a method in an interface.",
                ans3 = "The default case in a switch statement.",
                ans4 = "The default constructor of a class.",
                number = 37,
                correctAnswerIndex = "The default case in a switch statement.",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following will not cause a compile-time error in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "int x = 5; float y = 10.5; x = y;",
                ans2 = "int x = 10; float y = 20.5; x = (int) y;",
                ans3 = "String x = 10;",
                ans4 = "float y = \"10\";",
                number = 38,
                correctAnswerIndex = "int x = 10; float y = 20.5; x = (int) y;",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following is NOT a valid constructor in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "public MyClass() {}",
                ans2 = "private MyClass() {}",
                ans3 = "protected MyClass() {}",
                ans4 = "final MyClass() {}",
                number = 39,
                correctAnswerIndex = "final MyClass() {}",
                isIdentification = false
            ),

            Trivia(
                question = "Which of the following operators is used to compare two objects for equality in Java?",
                category = "Java",
                difficulty = "Medium",
                ans1 = "==",
                ans2 = "equals()",
                ans3 = "compareTo()",
                ans4 = "isEqual()",
                number = 40,
                correctAnswerIndex = "equals()",
                isIdentification = false
            ),
            Trivia(
                question = "Identify the exception thrown when trying to access an invalid index of an array in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "ArrayIndexOutOfBoundsException",
                ans2 = "IndexOutOfBoundsException",
                ans3 = "ArrayOutOfBoundsException",
                ans4 = "InvalidIndexException",
                number = 53,
                correctAnswerIndex = "ArrayIndexOutOfBoundsException",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the method used to find the maximum of two numbers in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Math.max()",
                ans2 = "Math.high()",
                ans3 = "Math.maximum()",
                ans4 = "max()",
                number = 54,
                correctAnswerIndex = "Math.max()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to define an interface in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "interface",
                ans2 = "abstract",
                ans3 = "implements",
                ans4 = "class",
                number = 55,
                correctAnswerIndex = "interface",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the method used to convert a string to a double in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Double.parseDouble()",
                ans2 = "parseDouble()",
                ans3 = "convertToDouble()",
                ans4 = "Double.toDouble()",
                number = 56,
                correctAnswerIndex = "Double.parseDouble()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the method used to parse an integer from a string in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Integer.parseInt()",
                ans2 = "parseInt()",
                ans3 = "convertToInt()",
                ans4 = "Integer.toInt()",
                number = 57,
                correctAnswerIndex = "Integer.parseInt()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the method used to get the character at a specified index of a string in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "charAt()",
                ans2 = "getChar()",
                ans3 = "characterAt()",
                ans4 = "charAtIndex()",
                number = 58,
                correctAnswerIndex = "charAt()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the loop that ensures that the code block runs at least once before the condition is checked in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "do-while",
                ans2 = "while",
                ans3 = "for",
                ans4 = "repeat",
                number = 59,
                correctAnswerIndex = "do-while",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the access modifier that provides the least visibility in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "private",
                ans2 = "protected",
                ans3 = "public",
                ans4 = "default",
                number = 60,
                correctAnswerIndex = "private",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the Java class used to handle user input from the console.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Scanner",
                ans2 = "BufferedReader",
                ans3 = "Console",
                ans4 = "InputStreamReader",
                number = 61,
                correctAnswerIndex = "Scanner",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the keyword used to prevent a class from being inherited in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "final",
                ans2 = "abstract",
                ans3 = "sealed",
                ans4 = "static",
                number = 62,
                correctAnswerIndex = "final",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the type of loop used when the number of iterations is known in advance in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "for",
                ans2 = "while",
                ans3 = "do-while",
                ans4 = "repeat",
                number = 63,
                correctAnswerIndex = "for",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the method used to check if a string contains a specific substring in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "contains()",
                ans2 = "hasSubstring()",
                ans3 = "substringExists()",
                ans4 = "checkSubstring()",
                number = 64,
                correctAnswerIndex = "contains()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the method used to convert an object to a string in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "toString()",
                ans2 = "convertToString()",
                ans3 = "objectToString()",
                ans4 = "stringify()",
                number = 65,
                correctAnswerIndex = "toString()",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the class used to generate random numbers in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "Random",
                ans2 = "Math",
                ans3 = "RandomGenerator",
                ans4 = "Math.random()",
                number = 66,
                correctAnswerIndex = "Random",
                isIdentification = true
            ),

            Trivia(
                question = "Identify the type of error that occurs when there is an attempt to divide by zero in Java.",
                category = "Java",
                difficulty = "Easy",
                ans1 = "ArithmeticException",
                ans2 = "ZeroDivisionError",
                ans3 = "DivideByZeroException",
                ans4 = "MathError",
                number = 67,
                correctAnswerIndex = "ArithmeticException",
                isIdentification = true
            )



        )
        triviaDao.insertTrivia(triviaList)
    }
}