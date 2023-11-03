package com.example.eldarwallet.feature_payment.domain.model

import android.os.Parcelable
import com.example.eldarwallet.feature_card.domain.model.CardDomainModel
import com.example.eldarwallet.feature_payment.data.local.entity.PaymentEntity
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentDomainModel(
    val userFullName: String,
    val paymentMethod: String,
    val card: CardDomainModel? = null,
    val totalAmount: Double
): Parcelable

fun PaymentDomainModel.toEntity() = PaymentEntity(
    userFullName = this.userFullName,
    paymentMethod = this.paymentMethod,
    card = Gson().toJson(this.card, CardDomainModel::class.java),
    totalAmount = totalAmount
)