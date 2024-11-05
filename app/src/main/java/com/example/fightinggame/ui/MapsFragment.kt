package com.example.fightinggame.ui

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fightinggame.R
import com.example.fightinggame.dao.LevelsDao
import com.example.fightinggame.dao.UserDao
import com.example.fightinggame.dao.UserPointsDao
import com.example.fightinggame.databinding.DialogEnterNameBinding
import com.example.fightinggame.databinding.FragmentMapsBinding
import com.example.fightinggame.db.CodexDatabase
import com.example.fightinggame.model.User
import com.example.fightinggame.model.mapsLevel
import com.example.fightinggame.util.LevelsViewModelFactory
import com.example.fightinggame.viewmodels.LevelsViewModel
import kotlinx.coroutines.launch

class MapsFragment : Fragment() {
    private lateinit var binding: FragmentMapsBinding
    private lateinit var levelsDao: LevelsDao
    private lateinit var user: UserDao
    private lateinit var levelsViewModel: LevelsViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userPointsDao: UserPointsDao
    private var mediaPlayer: MediaPlayer? = null
    private var isShown = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        sharedPreferences =
            requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        isShown = sharedPreferences.getBoolean("isShown", false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.maps)
        mediaPlayer?.isLooping = true // To loop the music
        mediaPlayer?.start()
        levelsDao = CodexDatabase.invoke(requireContext()).getMapsLevelDao()
        user = CodexDatabase.invoke(requireContext()).getUserName()
        userPointsDao = CodexDatabase.invoke(requireContext()).getUserPointsDao()
        val factory = LevelsViewModelFactory(levelsDao)
        levelsViewModel = ViewModelProvider(this, factory).get(LevelsViewModel::class.java)
        getUserName()
        showLevels()
        if (!isShown) {
            dialogOpenEnterName()

        }
        binding.home.setOnClickListener(){
            findNavController().navigate(R.id.homeFragment)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
    private fun getUserName() {
        viewLifecycleOwner.lifecycleScope.launch {
            val user = user.getUserById(1)
            if (user != null ) {
                binding.tvLevel.text = user.name
                getPointUser()
            } else {
                Log.d("UserName", "User or Points data is null")
                binding.tvLevel.text = "Player Name"
            }
        }
    }
    fun getPointUser(){
        viewLifecycleOwner.lifecycleScope.launch {
            val points = userPointsDao.getUserPoints(1)

            if (points != null ) {
                    binding.tvPoints.text = "Points: ${points.points}"
            } else {
                binding.tvPoints.text = "Points: 0"
            }
        }
    }


    private fun dialogOpenEnterName() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Use ViewBinding to inflate the custom layout
                val binding = DialogEnterNameBinding.inflate(LayoutInflater.from(requireContext()))

                // Create the AlertDialog
                val dialog = AlertDialog.Builder(requireContext())
                    .setView(binding.root)
                    .create()

                // Handle the submit button click
                binding.buttonSubmit.setOnClickListener {
                    val playerName = binding.editTextPlayerName.text.toString().trim()

                    if (playerName.isNotEmpty()) {
                        try {
                            // Save the player's name to the database
                            lifecycleScope.launch {
                                user.insertUser(User(1, playerName)) // Replace '1' with correct user ID if needed
                            }
                            Toast.makeText(
                                requireContext(),
                                "Player name saved: $playerName",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss() // Close the dialog
                            getUserName()
                            showDungeonAdventureDialog(requireContext())
                        } catch (e: Exception) {
                            Log.e("DialogError", "Error saving player name: ${e.message}", e)
                            Toast.makeText(
                                requireContext(),
                                "Failed to save player name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                // Show the dialog
                dialog.show()

            } catch (e: Exception) {
                Log.e("DialogError", "Error opening dialog: ${e.message}", e)
                Toast.makeText(requireContext(), "Error opening dialog", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLevels() {
        levelsViewModel.getAllCharacters { levels ->
            Log.d("ShowLevels", "Retrieved levels: $levels")
            bindCharactersToViews(levels)
        }
    }

    private fun bindCharactersToViews(levels: List<mapsLevel>) {
        val levelMarkers = listOf(
            binding.level1Marker,
            binding.level2Marker,
            binding.level3Marker,
            binding.level4Marker,
            binding.level5Marker,
        )

        for ((index, level) in levels.withIndex()) {
            if (index < levelMarkers.size) {
                val markerView = levelMarkers[index]

                if (level.status) {
                    if (level.isFinish) {
                        // If the level is finished, show a GIF and navigate to ReviewFragment
                        playFinishedLevelGif(markerView)
                        markerView.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "This level is already finished.",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Navigate to the ReviewFragment with the selected index
                            val bundle = Bundle().apply {
                                putInt("index", index + 1) // Pass the current index + 1
                            }
                            findNavController().navigate(R.id.reviewFragment, bundle)
                        }
                    } else {
                        // If the level is unlocked but not finished, show a GIF and navigate to BattleFragment
                        playGif(markerView)
                        markerView.setOnClickListener {
                            val adjustedIndex = index + 1
                            val bundle = Bundle().apply {
                                putInt("selected_level_index", adjustedIndex) // Pass the current index + 1
                            }
                            findNavController().navigate(R.id.battleFragment, bundle)
                        }
                    }
                } else {
                    // If the level is locked, show a message
                    markerView.setOnClickListener {
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
    // Play GIF on unlocked levels
    private fun playFinishedLevelGif(view: View) {
        Glide.with(this)
            .asGif()
            .load(R.drawable.dance)  // Assume torch is your gif resource
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
            .setPositiveButton("Continue") { dialog, _ ->
                dialog.dismiss()
                isShown = true
                sharedPreferences.edit().putBoolean("isShown", isShown).apply()
            }
            .setNegativeButton("Exit") { dialog, _ -> findNavController().navigateUp() }

        val dialog = dialogBuilder.create()

        // Apply custom background from the drawable resource
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        dialog.show()
    }

}
