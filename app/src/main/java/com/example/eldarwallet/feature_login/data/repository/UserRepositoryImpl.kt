package com.example.eldarwallet.feature_login.data.repository

import com.example.eldarwallet.feature_login.data.local.dao.UserDao
import com.example.eldarwallet.feature_login.data.local.entity.UserEntity
import com.example.eldarwallet.feature_login.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
): UserRepository {

    override suspend fun getUser(
        name: String,
        lastName: String,
    ): UserEntity? =
        userDao.getUser(name, lastName)

    override suspend fun insertUser(
        user: UserEntity
    ) {
        userDao.insertUser(user)
    }

}

