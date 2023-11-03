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
import com.example.eldarwallet.databinding.FragmentLoginBinding
import com.example.eldarwallet.feature_login.presentation.view_model.LoginViewModel
import com.example.eldarwallet.feature_login.utils.LoginState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        clearEntries()

        binding.btnLogin.setOnClickListener {
            loginButtonAction()
        }

        binding.btnRegister.setOnClickListener {
            registerButtonAction()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginUser.collect { loginState ->
                when (loginState) {
                    is LoginState.Success -> {
                        cleanErrors()
                        navToMainActivity()
                        viewModel.updateViewState(LoginState.LoadingOff)
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
        binding.etInputName.setText("")
        binding.etInputName.clearFocus()
        binding.etInputLastName.setText("")
        binding.etInputLastName.clearFocus()
        binding.etInputPassword.setText("")
        binding.etInputPassword.clearFocus()
    }


    private fun loginButtonAction() {
        viewModel.checkUserSelected(
            binding.etInputName.text.toString().trim(),
            binding.etInputLastName.text.toString().trim(),
            binding.etInputPassword.text.toString().trim()
        )
    }

    private fun registerButtonAction() {
        navToNewUserFragment()
    }


    private fun setUserNameError(errorMsg: String) {
        cleanErrors()
        binding.lInputName.error = errorMsg
    }

    private fun setUserLastNameError(errorMsg: String) {
        cleanErrors()
        binding.lInputLastName.error = errorMsg
    }

    private fun setUserPasswordError(errorMsg: String) {
        cleanErrors()
        binding.lInputPassword.error = errorMsg
    }

    private fun cleanErrors() {
        binding.lInputName.error = null
        binding.lInputLastName.error = null
        binding.lInputPassword.error = null
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun navToMainActivity() {
        val action = LoginFragmentDirections.actionLoginFragmentToMainActivity()
        findNavController().navigate(action)
    }

    private fun navToNewUserFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToNewUserFragment()
        findNavController().navigate(action)
    }


}