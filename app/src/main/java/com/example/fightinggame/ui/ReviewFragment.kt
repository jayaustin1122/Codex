package com.example.fightinggame.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fightinggame.adapter.ReviewAdapter
import com.example.fightinggame.dao.TriviaUserAnswerDao
import com.example.fightinggame.databinding.FragmentReviewBinding
import com.example.fightinggame.db.CodexDatabase
import kotlinx.coroutines.launch
class ReviewFragment : Fragment() {
    private lateinit var binding: FragmentReviewBinding
    private lateinit var triviaUserAnswerDao: TriviaUserAnswerDao
    private var selectedIndex: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        triviaUserAnswerDao = CodexDatabase.invoke(requireContext()).getTriviaQuestionUserAnswer()
        selectedIndex = arguments?.getInt("index") // index is passed as the level

        setupRecyclerView()
        fetchTriviaQuestionsByLevel()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fetchTriviaQuestionsByLevel() {
        lifecycleScope.launch {
            selectedIndex?.let { level ->
                // Fetch all trivia questions for the selected level
                val triviaList = triviaUserAnswerDao.getTriviaQuestionsById(level)
                if (triviaList.isNotEmpty()) {
                    val adapter = ReviewAdapter(triviaList)
                    binding.recyclerView.adapter = adapter
                } else {
                    // Handle case when no trivia questions are available
                    binding.noDataText.visibility = View.VISIBLE
                }
            }
        }
    }
}
