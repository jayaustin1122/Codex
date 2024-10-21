package com.example.fightinggame

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fightinggame.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }
    override fun onDestroyView() {
        super.onDestroyView()

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}