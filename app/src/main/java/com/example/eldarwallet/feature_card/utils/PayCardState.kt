package com.example.eldarwallet.feature_card.utils

import com.example.eldarwallet.feature_payment.domain.model.PaymentDomainModel

sealed class PayCardState {
    object LoadingOn : PayCardState()
    object LoadingOff : PayCardState()
    data class Success(val payment: PaymentDomainModel) : PayCardState()
    data class Error(val errorMsg: String) : PayCardState()

}

