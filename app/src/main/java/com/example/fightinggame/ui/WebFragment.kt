package com.example.fightinggame.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.example.fightinggame.R
import com.example.fightinggame.databinding.FragmentWebBinding


class WebFragment : Fragment() {
    private lateinit var webView: WebView
    private lateinit var binding : FragmentWebBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString("url")
        webView = binding.webView
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url ?: "https://www.example.com")
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState)
        }
    }
}
