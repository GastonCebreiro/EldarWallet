package com.example.eldarwallet.feature_login.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.R
import com.example.eldarwallet.feature_login.domain.model.UserDomainModel
import com.example.eldarwallet.feature_login.domain.use_case.InsertUserUseCase
import com.example.eldarwallet.feature_login.utils.LoginEvent
import com.example.eldarwallet.feature_login.utils.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewUserViewModel @Inject constructor(
    private val resources: Resources,
    private val insertUserUseCase: InsertUserUseCase
) : ViewModel() {

    private val _registerUser = MutableStateFlow<LoginState>(LoginState.LoadingOff)
    val registerUser: StateFlow<LoginState> = _registerUser

    fun updateViewState(newState: LoginState) {
        _registerUser.value = newState
    }

    fun checkUserToRegister(nameEntry: String, lastNameEntry: String, passwordEntry: String) {
        _registerUser.value = LoginState.LoadingOn
        when {
            nameEntry.isBlank() -> {
                _registerUser.value =
                    LoginState.WrongName(resources.getString(R.string.error_msg_enter_name))
                return
            }
            lastNameEntry.isBlank() -> {
                _registerUser.value =
                    LoginState.WrongLastName(resources.getString(R.string.error_msg_enter_lastname))
                return
            }
            passwordEntry.isBlank() -> {
                _registerUser.value =
                    LoginState.WrongPassword(resources.getString(R.string.error_msg_enter_password))
                return
            }
            else -> {
                registerUser(
                    name = nameEntry,
                    lastName = lastNameEntry,
                    password = passwordEntry
                )
            }
        }
    }

    private fun registerUser(
        name: String,
        lastName: String,
        password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val userToInsert = UserDomainModel(
                name = name,
                lastName = lastName,
                password = password
            )
            insertUserUseCase.invoke(userToInsert)
                .catch { e ->
                    val errorMsg =
                        resources.getString(R.string.error_msg_unknown) + e.message.toString()
                    _registerUser.value = LoginState.Error(errorMsg)
                }
                .collect { res ->
                    when (res) {
                        is LoginEvent.Success -> {
                            _registerUser.value = LoginState.Success(res.user)
                        }
                        is LoginEvent.Error -> {
                            _registerUser.value = LoginState.Error(res.errorMsg)
                        }
                        is LoginEvent.WrongPassword -> {}
                    }
                }
        }
    }
}

