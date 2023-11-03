package com.example.eldarwallet.feature_payment.domain.repository

import com.example.eldarwallet.feature_payment.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow


interface PaymentRepository {

    fun getPaymentsByUser(userFullName: String): Flow<List<PaymentEntity>>
    suspend fun insertPayment(payment: PaymentEntity)
}