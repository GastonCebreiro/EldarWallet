package com.example.eldarwallet.feature_login.utils

import com.example.eldarwallet.feature_login.domain.model.UserDomainModel

sealed class LoginState {
    object LoadingOn : LoginState()
    object LoadingOff : LoginState()
    data class Success(val user: UserDomainModel) : LoginState()
    data class Error(val errorMsg: String) : LoginState()
    data class WrongName(val errorMsg: String) : LoginState()
    data class WrongLastName(val errorMsg: String) : LoginState()
    data class WrongPassword(val errorMsg: String) : LoginState()
}