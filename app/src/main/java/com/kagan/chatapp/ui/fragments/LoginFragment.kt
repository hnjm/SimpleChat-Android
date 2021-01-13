package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentLoginBinding
import com.kagan.chatapp.utils.Utils.hideKeyboard
import com.kagan.chatapp.viewmodels.LoginViewModel
import com.kagan.chatapp.viewmodels.TokenPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val tokenPreferenceViewModel: TokenPreferenceViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        init()
    }

    private fun init() {
        setFocusChangeListener()

        binding.btnLogin.setOnClickListener {
            login()
            hideKeyboard(requireContext(), requireView())
        }

        binding.btnRegister.setOnClickListener {
            register()
        }

        binding.btnForgot.setOnClickListener {
            forgotPassword()
        }

        loginResult()
    }

    private fun loginResult() {
        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer
            Log.d(TAG, "loginResult: $loginResult")

            if (!loginResult) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                setVisibilityProgress(false)
            }
            if (loginResult) {
                setVisibilityProgress(false)
                Toast.makeText(context, "Navigate to Main Page", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun forgotPassword() {
        navigate(R.id.action_loginFragment_to_passwordRequestFragment)
    }

    private fun register() {
        navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun navigate(action: Int) {
        findNavController().navigate(action)
    }

    private fun login() {
        if (usernameIsNotEmpty() && passwordIsNotEmpty()) {
            clearErrorMessage()
            setVisibilityProgress(true)
            val username = binding.evUserName.editText?.text!!.toString()
            val password = binding.evPassword.editText?.text!!.toString()

//            loginViewModel.login(username, password)
        } else {
            setErrorMessage()
        }
    }

    private fun setVisibilityProgress(value: Boolean) {
        if (value) {
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.progress.visibility = View.INVISIBLE
        }
    }

    private fun setErrorMessage() {
        if (!usernameIsNotEmpty()) {
            binding.evUserName.error =
                getString(R.string.evErrorMessage, getString(R.string.user_name))
        } else {
            binding.evUserName.error = null
        }

        if (!passwordIsNotEmpty()) {
            binding.evPassword.error =
                getString(R.string.evErrorMessage, getString(R.string.password))
        } else {
            binding.evPassword.error = null
        }
    }

    private fun clearErrorMessage() {
        binding.evUserName.error = null
        binding.evPassword.error = null
    }

    private fun usernameIsNotEmpty(): Boolean {
        return binding.evUserName.editText?.text?.isNotEmpty()!!
    }

    private fun passwordIsNotEmpty(): Boolean {
        return binding.evPassword.editText?.text?.isNotEmpty()!!
    }

    private fun setFocusChangeListener() {
        val evUsername = binding.evUserName
        val evPassword = binding.evPassword

        evUsername.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                evUsername.error = null
            }
        }


        evPassword.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                evPassword.error = null
            }
        }
    }
}