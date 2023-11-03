package com.example.eldarwallet.core.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPreferencesUtil(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setUserLoggedName(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_LOGGED_NAME, name)
        editor.apply()
    }

    fun getUserLoggedName(): String? {
        return sharedPreferences.getString(USER_LOGGED_NAME, "")
    }

    fun setUserLoggedLastName(lastName: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_LOGGED_LASTNAME, lastName)
        editor.apply()
    }

    fun getUserLoggedLastName(): String? {
        return sharedPreferences.getString(USER_LOGGED_LASTNAME, "")
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES_NAME"
        const val USER_LOGGED_NAME = "USER_LOGGED_NAME"
        const val USER_LOGGED_LASTNAME = "USER_LOGGED_LASTNAME"
    }
}