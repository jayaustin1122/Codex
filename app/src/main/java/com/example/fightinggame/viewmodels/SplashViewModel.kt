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
    fun insertQuestionsAnswer(trivias: TriviaQuestionUserAnswer) {
        viewModelScope.launch {
            triviaUserAnswerDao.insertTriviaQuestion(trivias)
        }
    }
    fun retrieveQuestionsAnswer() {
        viewModelScope.launch {
            try {
                // Retrieve all trivia questions from the database
                val questions = triviaUserAnswerDao.getAllTriviaQuestions()

                // Log each question's details
                for (question in questions) {
                    Log.d("TriviaQuestion", "Question: ${question.question}, " +
                            "Answer1: ${question.ans1}, " +
                            "Answer2: ${question.ans2}, " +
                            "Answer3: ${question.ans3}, " +
                            "Answer4: ${question.ans4}, " +
                            "Correct Answer Index: ${question.correctAnswerIndex}, " +
                            "Level: ${question.level}")
                }
            } catch (e: Exception) {
                Log.e("TriviaViewModel", "Error retrieving questions: ${e.message}")
            }
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
                Character(1, "Hero1", 1, R.drawable.hero1, R.drawable.hero1_attack),
                Character(2, "Hero2", 1, R.drawable.hero2, R.drawable.hero1_attack),
                Character(3, "Hero3", 1, R.drawable.hero3, R.drawable.hero1_attack),
                Character(4, "Hero4", 1, R.drawable.hero4, R.drawable.hero1_attack)
            )

            // Insert characters
            for (character in charactersToInsert) {
                if (characterDao.getCharacterById(character.id) == null) {
                    characterDao.insertCharacters(character)
                }
            }

            val monstersToInsert = listOf(
                MonsterEnemy(1, 1, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(2, 2, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(3, 3, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(4, 4, R.drawable.hero1, R.drawable.hero1_attack),
                MonsterEnemy(5, 5, R.drawable.hero1, R.drawable.hero1_attack),
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
                correctAnswerIndex = "0"
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
                correctAnswerIndex = "{ and }"
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
                correctAnswerIndex = "=="
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
                correctAnswerIndex = "if (expression)" // Answer is if (expression)
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
                correctAnswerIndex = "&&" // Answer is &&
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
                correctAnswerIndex = "!=" // Answer is !=
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
                correctAnswerIndex = "!" // Answer is !
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
                correctAnswerIndex = "real" // Answer is real
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
                correctAnswerIndex = "string" // Answer is string
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
                correctAnswerIndex = "[]" // Answer is []
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
                correctAnswerIndex = "*" // Answer is *
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
                correctAnswerIndex = "return" // Answer is return
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
                correctAnswerIndex = "break" // Answer is break
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
                correctAnswerIndex = "std::reverse(s.begin(), s.end());" // Answer is std::reverse(s.begin(), s.end())
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
                correctAnswerIndex = "int factorial(int n) { return n * factorial(n - 1); }" // Answer is int factorial(int n) { return n * factorial(n - 1); }
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
                correctAnswerIndex = "interface Shape { getArea(); getPerimeter(); } class Rectangle implements Shape {...}" // Answer is interface Shape { getArea(); getPerimeter(); } class Rectangle implements Shape {...}
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
                correctAnswerIndex = "int gcd(int a, int b) {...}" // Answer is int gcd(int a, int b) {...}
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
                correctAnswerIndex = "Integer.parseInt(binaryString, 2);" // Answer is Integer.parseInt(binaryString, 2);
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
                correctAnswerIndex = "return s == string(s.rbegin(), s.rend());"
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
                correctAnswerIndex = "void add(int a, int b) {...} void add(double a, b) {...}"
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
                correctAnswerIndex = "std::sort(arr.begin(), arr.end(), [](int a, int b) {...});"
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
                correctAnswerIndex = "class Car extends Vehicle {...}"
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
                correctAnswerIndex = "virtual void print() {...}"
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
                correctAnswerIndex = "BufferedReader reader = new BufferedReader(...);"
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
                correctAnswerIndex = "Complex operator+(const Complex& c) {...}"
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
                correctAnswerIndex = "std::cout << \"Hello, World!\";"
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
                correctAnswerIndex = "System.out.println(\"Hello, World!\");"
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
                correctAnswerIndex = "int sum(int a, int b) { return a + b; }"
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
                correctAnswerIndex = "int sum(int a, int b) { return a + b; }"
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
                correctAnswerIndex = "return (a > b) ? a : b;"
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
                correctAnswerIndex = "return Math.max(a, b);"
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
                correctAnswerIndex = "return n % 2 == 0;"
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
                correctAnswerIndex = "return n % 2 == 0;"
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
                correctAnswerIndex = "return n * n;"
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
                correctAnswerIndex = "return n * n;"
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
                correctAnswerIndex = "swap(a, b);"
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
                correctAnswerIndex = "temp = a; a = b; b = temp;"
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
                correctAnswerIndex = "return n > 0;"
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
                correctAnswerIndex = "return n > 0;"
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
                correctAnswerIndex = "for(int i=1; i<=10; i++) cout << i;"
            )
        )
        triviaDao.insertTrivia(triviaList)
    }
}