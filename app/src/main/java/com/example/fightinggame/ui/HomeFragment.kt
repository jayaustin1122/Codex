package com.example.fightinggame.ui

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fightinggame.R
import com.example.fightinggame.dao.CharacterDao
import com.example.fightinggame.dao.CharacterSelectionDao
import com.example.fightinggame.databinding.FragmentHomeBinding
import com.example.fightinggame.db.CodexDatabase
import com.example.fightinggame.model.User
import com.example.fightinggame.model.UserPoints
import com.example.fightinggame.util.SplashViewModelFactory
import com.example.fightinggame.viewmodels.SplashViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var characterDao: CharacterDao
    private lateinit var selectionDao: CharacterSelectionDao
    private lateinit var sharedPreferences: SharedPreferences
    private var isShown = false
    private val viewModel: SplashViewModel by viewModels { SplashViewModelFactory(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        sharedPreferences =
            requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        isShown = sharedPreferences.getBoolean("isShown", false)
        return binding.root
    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterDao =CodexDatabase.invoke(requireContext()).getCharacterDao()
        selectionDao =CodexDatabase.invoke(requireContext()).getCharacterSelectionDao()
        playGif()
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.samplemusicbg)
        mediaPlayer?.isLooping = true // To loop the music
        mediaPlayer?.start()
        checkSelectedCharacter()

        binding.newRunButton.setOnClickListener {
            if (binding.continueButton.visibility == View.VISIBLE) {

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.updatePoints(UserPoints(1, 0))
                    viewModel.insertUser(User(1, "playerName"))
                    viewModel.insertData()
                }
                overwriteGameProgress()
                isShown = false
                sharedPreferences.edit().putBoolean("isShown", isShown).apply()
            } else {
                openSelectCharacterFragment()
            }
        }

        binding.exitButton.setOnClickListener {
            requireActivity().finish()
        }

        binding.continueButton.setOnClickListener {
            findNavController().navigate(R.id.mapsFragment)
        }

    }
    private fun openSelectCharacterFragment() {
        val selectCharacterFragment = SelectCharacterFragment()
        selectCharacterFragment.setTargetFragment(this, 0)
        selectCharacterFragment.show(parentFragmentManager, "com.example.fightinggame.ui.SelectCharacterFragment")
    }

    private fun overwriteGameProgress() {
        GlobalScope.launch(Dispatchers.IO) {
            requireActivity().runOnUiThread {
                openSelectCharacterFragment()
            }
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun checkSelectedCharacter() {
        GlobalScope.launch(Dispatchers.IO) {
            val selectedCharacters = selectionDao.getAllCharacterSelections()
            if (selectedCharacters.isNotEmpty()) {
                // There's already a selected character
                requireActivity().runOnUiThread {
                    binding.continueButton.visibility = View.VISIBLE
                   // binding.newRunButton.text = "New Game (Overwrite)"
                }
            } else {
                // No selected character
                requireActivity().runOnUiThread {
                    binding.continueButton.visibility = View.GONE
              //      binding.newRunButton.text = "New Game"
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
    private fun playGif() {
        val torchViews = listOf(binding.torch1, binding.torch2, binding.torch3, binding.torch4)
        torchViews.forEach { torchView ->
            Glide.with(this)
                .asGif()
                .load(R.drawable.torch)
                .into(torchView)
        }
    }


}
