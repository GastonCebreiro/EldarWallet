package com.example.eldarwallet.feature_login.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eldarwallet.R
import com.example.eldarwallet.databinding.FragmentNewUserBinding
import com.example.eldarwallet.feature_login.presentation.view_model.NewUserViewModel
import com.example.eldarwallet.feature_login.utils.LoginState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewUserFragment : Fragment() {

    private val viewModel: NewUserViewModel by viewModels()

    private lateinit var binding: FragmentNewUserBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewUserBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        clearEntries()

        binding.btnSave.setOnClickListener {
            saveButtonAction()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.registerUser.collect { loginState ->
                when (loginState) {
                    is LoginState.Success -> {
                        cleanErrors()
                        showMessage(resources.getString(R.string.success_msg_user_registered))
                        viewModel.updateViewState(LoginState.LoadingOff)
                        navToLoginFragment()
                    }
                    is LoginState.Error -> {
                        cleanErrors()
                        showMessage(loginState.errorMsg)
                        viewModel.updateViewState(LoginState.LoadingOff)
                    }
                    is LoginState.WrongName -> {
                        setUserNameError(loginState.errorMsg)
                        showMessage(loginState.errorMsg)
                        viewModel.updateViewState(LoginState.LoadingOff)
                    }
                    is LoginState.WrongLastName -> {
                        setUserLastNameError(loginState.errorMsg)
                        showMessage(loginState.errorMsg)
                        viewModel.updateViewState(LoginState.LoadingOff)
                    }
                    is LoginState.WrongPassword -> {
                        setUserPasswordError(loginState.errorMsg)
                        showMessage(loginState.errorMsg)
                        viewModel.updateViewState(LoginState.LoadingOff)
                    }
                    is LoginState.LoadingOn -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is LoginState.LoadingOff -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

    }


    private fun clearEntries() {
        binding.etUserName.setText("")
        binding.etUserLastName.setText("")
        binding.etUserLastName.clearFocus()
        binding.etUserPassword.setText("")
        binding.etUserPassword.clearFocus()
    }

    private fun saveButtonAction() {
        viewModel.checkUserToRegister(
            binding.etUserName.text.toString().trim(),
            binding.etUserLastName.text.toString().trim(),
            binding.etUserPassword.text.toString().trim()
        )
    }

    private fun setUserNameError(errorMsg: String) {
        cleanErrors()
        binding.lUserName.error = errorMsg
    }

    private fun setUserLastNameError(errorMsg: String) {
        cleanErrors()
        binding.lUserLastName.error = errorMsg
    }

    private fun setUserPasswordError(errorMsg: String) {
        cleanErrors()
        binding.lUserPassword.error = errorMsg
    }

    private fun cleanErrors() {
        binding.lUserName.error = null
        binding.lUserLastName.error = null
        binding.lUserPassword.error = null
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun navToLoginFragment() {
        findNavController().navigateUp()
    }

}