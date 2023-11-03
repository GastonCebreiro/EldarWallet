package com.example.eldarwallet.feature_login.di

import android.content.Context
import android.content.res.Resources
import com.example.eldarwallet.core.data.AppDatabase
import com.example.eldarwallet.feature_login.data.local.dao.UserDao
import com.example.eldarwallet.feature_login.data.repository.UserRepositoryImpl
import com.example.eldarwallet.feature_login.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Singleton
    @Provides
    fun providesUserDao(db: AppDatabase): UserDao = db.userDao()

    @Singleton
    @Provides
    fun providesUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository =
        userRepositoryImpl
}