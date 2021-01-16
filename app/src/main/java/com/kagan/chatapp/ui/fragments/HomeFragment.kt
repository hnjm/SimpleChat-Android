package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentHomeBinding
import com.kagan.chatapp.viewmodels.LoginViewModel
import com.kagan.chatapp.viewmodels.TokenPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val tokenPreferenceViewModel: TokenPreferenceViewModel by viewModels()
    private lateinit var accessToken: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        init()
        subscribe()
    }

    private fun subscribe() {
        tokenPreferenceViewModel.accessToken.observe(viewLifecycleOwner, Observer {
            val accessToken = it ?: return@Observer
            this.accessToken = accessToken
        })

    }

    private fun init() {
        binding.btnSignOut.setOnClickListener {
            loginViewModel.logout(accessToken)
            tokenPreferenceViewModel.storeRefreshToken("")
        }
    }
}