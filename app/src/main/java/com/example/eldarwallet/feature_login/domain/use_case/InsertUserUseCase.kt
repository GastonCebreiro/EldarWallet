package com.example.eldarwallet.feature_login.domain.use_case

import android.content.res.Resources
import com.example.eldarwallet.R
import com.example.eldarwallet.feature_login.domain.model.UserDomainModel
import com.example.eldarwallet.feature_login.domain.model.toEntity
import com.example.eldarwallet.feature_login.domain.repository.UserRepository
import com.example.eldarwallet.feature_login.utils.LoginEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(
    private val resources: Resources,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userToInsert: UserDomainModel
    ): Flow<LoginEvent> = flow {
        try {
            userRepository.insertUser(userToInsert.toEntity())
            emit(LoginEvent.Success(userToInsert))
        } catch (e: Exception) {
            val errorMsg = resources.getString(R.string.error_msg_unknown) + e.message
            emit(LoginEvent.Error(errorMsg))
        }
    }
}