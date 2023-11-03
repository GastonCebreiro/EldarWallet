package com.example.eldarwallet.feature_card.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardDomainModel(
    var cardNumber: String,
    var cardCode: String,
    var expireDate: String,
    var fullName: String,
    var cardType: String
): Parcelable
