package com.example.eldarwallet.feature_login.domain.repository

import com.example.eldarwallet.feature_login.data.local.entity.UserEntity


interface UserRepository {

    suspend fun getUser(name: String, lastName: String): UserEntity?
    suspend fun insertUser(user: UserEntity)
}