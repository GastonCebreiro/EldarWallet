package com.example.eldarwallet.feature_card.utils

import com.example.eldarwallet.feature_card.domain.model.CardDomainModel

sealed class CardEvent {
    data class Success(val card: CardDomainModel) : CardEvent()
    data class Error(val errorMsg: String) : CardEvent()
}