package com.example.fightinggame.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fightinggame.databinding.ItemTriviaReviewBinding
import com.example.fightinggame.model.TriviaQuestionUserAnswer

class ReviewAdapter(
    private val triviaList: List<TriviaQuestionUserAnswer>
) : RecyclerView.Adapter<ReviewAdapter.TriviaViewHolder>() {

    class TriviaViewHolder(val binding: ItemTriviaReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trivia: TriviaQuestionUserAnswer) {
            binding.questionText.text = "Question: ${trivia.question}"
            binding.answer1Text.text = "Answer1: ${trivia.ans1}"
            binding.answer2Text.text = "Answer2: ${trivia.ans2}"
            binding.answer3Text.text = "Answer3: ${trivia.ans3}"
            binding.answer4Text.text = "Answer4: ${trivia.ans4}"
            binding.correctAnswerText.text = "Correct Answer: ${trivia.correctAnswerIndex}"
            binding.userSelectedAnswerText.text = "Your Answer: ${trivia.userSelectAnswer}"
            binding.levelText.text = "Level: ${trivia.level}"

            // Highlight the user's answer if it is correct or wrong
            if (trivia.userSelectAnswer == trivia.correctAnswerIndex) {
                binding.userSelectedAnswerText.setTextColor(binding.root.context.getColor(android.R.color.holo_green_dark))
            } else {
                binding.userSelectedAnswerText.setTextColor(binding.root.context.getColor(android.R.color.holo_red_dark))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriviaViewHolder {
        val binding = ItemTriviaReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TriviaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TriviaViewHolder, position: Int) {
        val trivia = triviaList[position]
        holder.bind(trivia)
    }

    override fun getItemCount(): Int = triviaList.size
}
