package com.example.eldarwallet.feature_login.domain.use_case

import android.content.res.Resources
import com.example.eldarwallet.R
import com.example.eldarwallet.feature_login.data.local.entity.toDomainModel
import com.example.eldarwallet.feature_login.domain.repository.UserRepository
import com.example.eldarwallet.feature_login.utils.LoginEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val resources: Resources,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        name: String,
        lastName: String,
        password: String
    ): Flow<LoginEvent> = flow {
        val user = userRepository.getUser(name, lastName)?.toDomainModel()
        if (user == null) {
            emit(LoginEvent.Error(resources.getString(R.string.error_msg_new_user_not_found)))
            return@flow
        }
        if(user.password != password) {
            emit(LoginEvent.WrongPassword(resources.getString(R.string.error_msg_invalid_password)))
            return@flow
        }
        emit(LoginEvent.Success(user))
    }
}