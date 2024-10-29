package com.example.fightinggame.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fightinggame.R
import com.example.fightinggame.dao.CharacterDao
import com.example.fightinggame.databinding.FragmentHomeBinding
import com.example.fightinggame.db.CodexDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var characterDao: CharacterDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterDao =CodexDatabase.invoke(requireContext()).getCharacterDao()
        playGif()
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.samplemusicbg)
        mediaPlayer?.isLooping = true // To loop the music
        mediaPlayer?.start()
        binding.newRunButton.setOnClickListener {
            val selectCharacterFragment = SelectCharacterFragment()
            selectCharacterFragment.setTargetFragment(this, 0)
            selectCharacterFragment.show(parentFragmentManager, "SelectCharacterFragment")
        }
        binding.exitButton.setOnClickListener {
            requireActivity().finish()
        }
        binding.continueButton.setOnClickListener {
            Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
        }
        GlobalScope.launch(Dispatchers.IO) {
            val character = characterDao.getCharacterById(3)
            if (character != null) {
                Log.d("HomeFragment", "Character Retrieved: ID=${character.id}, Name=${character.name}")
            } else {
                Log.d("HomeFragment", "No character found with ID=1")
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