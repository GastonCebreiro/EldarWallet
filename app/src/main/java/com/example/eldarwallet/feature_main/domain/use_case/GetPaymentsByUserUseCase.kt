package com.example.eldarwallet.feature_main.domain.use_case

import android.util.Log
import com.example.eldarwallet.feature_card.domain.model.CardDomainModel
import com.example.eldarwallet.feature_card.domain.repository.CardRepository
import com.example.eldarwallet.feature_card.utils.CryptoManager
import com.example.eldarwallet.feature_payment.data.local.entity.toDomainModelList
import com.example.eldarwallet.feature_payment.domain.model.PaymentDomainModel
import com.example.eldarwallet.feature_payment.domain.repository.PaymentRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPaymentsByUserUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {

    operator fun invoke(userFullName: String): Flow<List<PaymentDomainModel>> {
        return paymentRepository.getPaymentsByUser(userFullName). map {
            it.toDomainModelList()
        }
    }
}