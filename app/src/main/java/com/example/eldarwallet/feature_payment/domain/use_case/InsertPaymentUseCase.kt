package com.example.eldarwallet.feature_payment.domain.use_case

import android.content.res.Resources
import com.example.eldarwallet.R
import com.example.eldarwallet.feature_payment.domain.model.PaymentDomainModel
import com.example.eldarwallet.feature_payment.domain.model.toEntity
import com.example.eldarwallet.feature_payment.domain.repository.PaymentRepository
import com.example.eldarwallet.feature_payment.utils.PaymentEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertPaymentUseCase @Inject constructor(
    private val resources: Resources,
    private val paymentRepository: PaymentRepository
) {

    suspend operator fun invoke(
        paymentToInsert: PaymentDomainModel
    ): Flow<PaymentEvent> = flow {
        try {
            paymentRepository.insertPayment(paymentToInsert.toEntity())
            emit(PaymentEvent.Success(paymentToInsert))
        } catch (e: Exception) {
            val errorMsg = resources.getString(R.string.error_msg_unknown) + e.message
            emit(PaymentEvent.Error(errorMsg))
        }
    }
}