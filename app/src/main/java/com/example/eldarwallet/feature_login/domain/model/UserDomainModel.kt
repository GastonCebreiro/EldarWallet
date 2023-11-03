package com.example.eldarwallet.feature_login.domain.model

import com.example.eldarwallet.feature_login.data.local.entity.UserEntity


data class UserDomainModel(
    var name: String,
    var lastName: String,
    var password: String
)

fun UserDomainModel.toEntity() = UserEntity(
    name = this.name,
    lastName = this.lastName,
    password = this.password
)