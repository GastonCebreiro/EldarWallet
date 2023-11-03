package com.example.eldarwallet.feature_login.domain.use_case

import com.example.eldarwallet.core.utils.SharedPreferencesUtil
import javax.inject.Inject

class GetUserLoggedFullNameUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferencesUtil,
) {

    operator fun invoke(): String {
        val userName = sharedPreferences.getUserLoggedName().orEmpty()
        val userLastName = sharedPreferences.getUserLoggedLastName().orEmpty()
        return "$userName $userLastName"
    }
}