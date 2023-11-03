package com.example.eldarwallet.feature_login.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.R
import com.example.eldarwallet.core.utils.SharedPreferencesUtil
import com.example.eldarwallet.feature_login.domain.model.UserDomainModel
import com.example.eldarwallet.feature_login.domain.use_case.GetUserUseCase
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
class LoginViewModel @Inject constructor(
    private val resources: Resources,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _loginUser = MutableStateFlow<LoginState>(LoginState.LoadingOff)
    val loginUser: StateFlow<LoginState> = _loginUser

    fun updateViewState(newState: LoginState) {
        _loginUser.value = newState
    }

    fun checkUserSelected(nameEntry: String, lastNameEntry: String, passwordEntry: String) {
        _loginUser.value = LoginState.LoadingOn
        when {
            nameEntry.isBlank() -> {
                _loginUser.value =
                    LoginState.WrongName(resources.getString(R.string.error_msg_enter_name))
                return
            }
            lastNameEntry.isBlank() -> {
                _loginUser.value =
                    LoginState.WrongLastName(resources.getString(R.string.error_msg_enter_lastname))
                return
            }
            passwordEntry.isBlank() -> {
                _loginUser.value =
                    LoginState.WrongPassword(resources.getString(R.string.error_msg_enter_password))
                return
            }
            else -> {
                getUserSelected(
                    name = nameEntry,
                    lastName = lastNameEntry,
                    password = passwordEntry
                )
            }
        }
    }

    private fun getUserSelected(
        name: String,
        lastName: String,
        password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase.invoke(name, lastName, password)
                .catch { e ->
                    val errorMsg =
                        resources.getString(R.string.error_msg_unknown) + e.message.toString()
                    _loginUser.value = LoginState.Error(errorMsg)
                }
                .collect { res ->
                    when (res) {
                        is LoginEvent.Success -> {
                            saveUserLogged(res.user)
                            _loginUser.value = LoginState.Success(res.user)
                        }
                        is LoginEvent.Error -> {
                            _loginUser.value = LoginState.Error(res.errorMsg)
                        }
                        is LoginEvent.WrongPassword -> {
                            _loginUser.value = LoginState.WrongPassword(res.errorMsg)
                        }
                    }
                }
        }
    }

    private fun saveUserLogged(user: UserDomainModel) {
        sharedPreferencesUtil.setUserLoggedName(user.name)
        sharedPreferencesUtil.setUserLoggedLastName(user.lastName)
    }

}