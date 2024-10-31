package com.example.fightinggame.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fightinggame.R
import com.example.fightinggame.databinding.FragmentSplashBinding
import com.example.fightinggame.util.SplashViewModelFactory
import com.example.fightinggame.viewmodels.SplashViewModel

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private var progressStatus = 0
    private lateinit var handler: Handler
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: SplashViewModel by viewModels { SplashViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper())
        simulateProgress()

        // Restore the insertion state if it exists
        val isDataInserted = sharedPreferences.getBoolean("isDataInserted", false)

        // Observe ViewModel for data insertion completion
        viewModel.isDataInserted.observe(viewLifecycleOwner, Observer { dataInserted ->
            if (dataInserted) {
                sharedPreferences.edit().putBoolean("isDataInserted", true).apply()
            }
        })

        // Only insert data if it hasn't been inserted yet
        if (!isDataInserted) {
            viewModel.insertData()
        }

        // Navigate to HomeFragment after 5 seconds
        handler.postDelayed({
            findNavController().navigate(R.id.homeFragment)
        }, 5000)
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
}
