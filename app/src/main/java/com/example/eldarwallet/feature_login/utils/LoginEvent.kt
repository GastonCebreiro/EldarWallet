package com.example.eldarwallet.feature_login.utils

import com.example.eldarwallet.feature_login.domain.model.UserDomainModel

sealed class LoginEvent {
    data class Success(val user: UserDomainModel) : LoginEvent()
    data class Error(val errorMsg: String) : LoginEvent()
    data class WrongPassword(val errorMsg: String) : LoginEvent()
}