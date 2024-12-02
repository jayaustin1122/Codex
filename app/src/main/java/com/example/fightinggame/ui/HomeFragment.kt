package com.example.fightinggame.ui

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
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
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.splash)
        mediaPlayer?.isLooping = true // To loop the music
        mediaPlayer?.start()
        checkSelectedCharacter()

        binding.newRunButton.setOnClickListener {
            if (binding.continueButton.visibility == View.VISIBLE) {

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.updatePoints(UserPoints(1, 100))
                    viewModel.insertUser(User(1, "playerName"))
                    viewModel.insertData()
                    viewModel.deleteAllAnswers()
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
        binding.ins.setOnClickListener {
            openDialog()
        }

    }

    private fun openDialog() {
        val languages = arrayOf("Java", "C++")

        // Create a custom adapter to set the item layout
        val adapter = object : ArrayAdapter<String>(requireContext(), R.layout.dialog_list_item, languages) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.dialogItem)
                textView.text = languages[position]
                return view
            }
        }

        val dialogBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialogTheme)
            .setTitle("Select a language")
            .setAdapter(adapter) { dialog, which ->
                val selectedLanguage = languages[which]
                openWebFragment(selectedLanguage) // Pass the selected language
                dialog.dismiss()
            }
            .setNegativeButton("Exit") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        dialog.show()
    }


    private fun openWebFragment(selectedLanguage: String) {
        val url = when (selectedLanguage) {
            "Java" -> "https://www.w3schools.com/java/"
            "C++" -> "https://www.w3schools.com/cpp/"
            else -> "https://www.example.com"
        }

        val bundle = Bundle().apply {
            putString("url", url)
        }
        findNavController().navigate(R.id.webFragment, bundle)
    }


    private fun openSelectCharacterFragment() {
        val selectCharacterFragment = SelectCharacterFragment()
        selectCharacterFragment.setTargetFragment(this, 0)
        selectCharacterFragment.show(parentFragmentManager, "com.example.fightinggame.ui.SelectCharacterFragment")
        mediaPlayer?.stop()
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
