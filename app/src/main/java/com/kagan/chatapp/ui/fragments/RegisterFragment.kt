package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var evUsername: EditText
    private lateinit var evDisplayName: EditText
    private lateinit var evEmail: EditText
    private lateinit var evPassword: EditText
    private lateinit var evConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        setVariables()
        init()
    }

    private fun setVariables() {
        evUsername = binding.evUserName.editText!!
        evDisplayName = binding.evDisplayName.editText!!
        evEmail = binding.evEmail.editText!!
        evPassword = binding.evPassword.editText!!
        evConfirmPassword = binding.evConfirmPassword.editText!!
    }

    private fun init() {
        binding.btnRegister.setOnClickListener {
            isEmpty()
        }

        binding.btnGoBack.setOnClickListener {
            navigateUp()
        }
    }

    private fun navigateUp() {
        findNavController().navigateUp()
    }

    private fun navigate(action: Int) {
        findNavController().navigate(action)
    }

    private fun isEmpty() {
        if (evUsername.text?.isEmpty() == true) {
            binding.evUserName.error = getString(R.string.evErrorMessage, getString(R.string.user_name))
        } else {
            binding.evUserName.error = getString(R.string.evErrorMessage, getString(R.string.user_name))
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
    }
}