package com.example.eldarwallet.feature_payment.utils

import com.example.eldarwallet.feature_payment.domain.model.PaymentDomainModel

sealed class PaymentEvent {
    data class Success(val payment: PaymentDomainModel) : PaymentEvent()
    data class Error(val errorMsg: String) : PaymentEvent()
}