package com.example.fightinggame.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fightinggame.R
import com.example.fightinggame.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private var progressStatus = 0
    private lateinit var handler: Handler

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
