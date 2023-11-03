package com.example.eldarwallet.feature_payment.data.repository

import com.example.eldarwallet.feature_payment.data.local.dao.PaymentDao
import com.example.eldarwallet.feature_payment.data.local.entity.PaymentEntity
import com.example.eldarwallet.feature_payment.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentDao: PaymentDao
) : PaymentRepository {

    override fun getPaymentsByUser(userFullName: String): Flow<List<PaymentEntity>> =
        paymentDao.getPaymentsByUser(userFullName)


    override suspend fun insertPayment(payment: PaymentEntity) {
        paymentDao.insertPayment(payment)
    }

}

