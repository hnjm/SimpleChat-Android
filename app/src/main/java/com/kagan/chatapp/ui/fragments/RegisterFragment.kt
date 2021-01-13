package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentRegisterBinding
import com.kagan.chatapp.models.RegisterUserRequestVM
import com.kagan.chatapp.models.RegisterUserVM
import com.kagan.chatapp.repositories.LoginRepository
import com.kagan.chatapp.utils.ErrorCodes.getDescription
import com.kagan.chatapp.utils.Utils.hideKeyboard
import com.kagan.chatapp.viewmodels.LoginViewModel
import com.kagan.chatapp.viewmodels.viewmodelfactory.LoginViewModelFactory

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var repository: LoginRepository
    private lateinit var factory: LoginViewModelFactory
    private lateinit var loginViewModel: LoginViewModel

    private lateinit var evUsername: EditText
    private lateinit var evDisplayName: EditText
    private lateinit var evEmail: EditText
    private lateinit var evPassword: EditText
    private lateinit var evConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = LoginRepository()
        factory = LoginViewModelFactory(repository)
        loginViewModel =
            ViewModelProvider(requireActivity(), factory).get(LoginViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        setVariables()
        setOnClickListener()
        setOnFocusChangeListener()
        registerResultObserve()
    }

    private fun setVariables() {
        evUsername = binding.evUserName.editText!!
        evDisplayName = binding.evDisplayName.editText!!
        evEmail = binding.evEmail.editText!!
        evPassword = binding.evPassword.editText!!
        evConfirmPassword = binding.evConfirmPassword.editText!!
    }

    private fun setOnClickListener() {

        binding.btnRegister.setOnClickListener {
            hideKeyboard(requireContext(), it)
            setVisibilityProgress(true)
            if (isNotEmpty() && isSamePassword()) {
                register()
            }
        }

        binding.btnGoBack.setOnClickListener {
            navigateUp()
        }
    }

    private fun navigateUp() {
        findNavController().navigateUp()
    }

    private fun navigate() {
        navigateUp()
    }

    private fun isNotEmpty(): Boolean {
        if (evUsername.text?.isEmpty() == true) {
            binding.evUserName.error =
                getString(R.string.evErrorMessage, getString(R.string.user_name))
        } else {
            binding.evUserName.error = null
        }

        if (evDisplayName.text?.isEmpty() == true) {
            binding.evDisplayName.error = "evDisplayName"
        } else {
            binding.evDisplayName.error = null
        }

        if (evEmail.text?.isEmpty() == true) {
            binding.evEmail.error = "evDisplayName"
        } else {
            binding.evEmail.error = null
        }

        if (evPassword.text?.isEmpty() == true) {
            binding.evPassword.error = "evDisplayName"
        } else {
            binding.evPassword.error = null
        }

        if (evConfirmPassword.text?.isEmpty() == true) {
            binding.evConfirmPassword.error = "evDisplayName"
        } else {
            binding.evConfirmPassword.error = null
        }

        return evUsername.text?.isNotEmpty() == true && evDisplayName.text?.isNotEmpty() == true
                && evEmail.text?.isNotEmpty() == true && evPassword.text?.isNotEmpty() == true
                && evConfirmPassword.text?.isNotEmpty() == true
    }

    private fun setOnFocusChangeListener() {
        val evUsername = binding.evUserName
        val evDisplayName = binding.evDisplayName
        val evEmail = binding.evEmail
        val evPassword = binding.evPassword
        val evConfirmPassword = binding.evConfirmPassword

        evUsername.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                evUsername.error = null
            }
        }

        evDisplayName.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                evDisplayName.error = null
            }
        }

        evEmail.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                evEmail.error = null
            }
        }

        evPassword.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                evPassword.error = null
            }
        }

        evConfirmPassword.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                evConfirmPassword.error = null
            }
        }
    }

    private fun registerResultObserve() {
        loginViewModel.registerResultWithRecVM.observe(viewLifecycleOwner, Observer { result ->
            val registerResult = result ?: return@Observer

            if (result.isSuccessful) {
                Log.d(TAG, "registerResultObserve: ${registerResult.rec}")
                setVisibilityProgress(false)
//                navigate()
            }
        })

        loginViewModel.registerErrors.observe(viewLifecycleOwner, Observer { result ->
            val registerErrors = result ?: return@Observer
            registerErrors.errors?.forEach {
                when (it.field) {
                    "UserName" -> {
                        val text = getDescription(it.errorCode)
                        binding.evUserName.error = text
                    }
                    "Password" -> {
                        val text = getDescription(it.errorCode)
                        binding.evPassword.error = text
                    }
                    "ConfirmPassword" -> {
                        val text = getDescription(it.errorCode)
                        binding.evConfirmPassword.error = text
                    }
                    "DisplayName" -> {
                        val text = getDescription(it.errorCode)
                        binding.evDisplayName.error = text
                    }
                    "Email" -> {
                        val text = getDescription(it.errorCode)
                        binding.evEmail.error = text
                    }
                }
            }
        })
    }


    private fun setVisibilityProgress(value: Boolean) {
        if (value) {
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.progress.visibility = View.INVISIBLE
        }
        Snackbar.make(requireView(), "Show progress bar.", Snackbar.LENGTH_SHORT).show()
    }

    private fun register() {
        val username = evUsername.text.toString()
        val displayName = evDisplayName.text.toString()
        val email = evEmail.text.toString()
        val password = evPassword.text.toString()
        val confirmPassword = evConfirmPassword.text.toString()

        val newUser = RegisterUserVM(
            username = username,
            password = password,
            confirmPassword = confirmPassword,
            displayName = displayName,
            email = email
        )

        val request = RegisterUserRequestVM()
        request.requestBody = newUser

        loginViewModel.register(request)
    }

    private fun isSamePassword(): Boolean {
        val password = evPassword.text.toString()
        val confirmPassword = evConfirmPassword.text.toString()
        return password == confirmPassword
    }
}