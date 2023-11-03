package com.example.eldarwallet.feature_card.utils

import com.example.eldarwallet.feature_card.domain.model.CardDomainModel

sealed class CardState {
    object LoadingOn : CardState()
    object LoadingOff : CardState()
    data class Success(val card: CardDomainModel) : CardState()
    data class Error(val errorMsg: String) : CardState()
    data class WrongCardNumber(val errorMsg: String) : CardState()
    data class WrongCardCode(val errorMsg: String) : CardState()
    data class WrongCardDate(val errorMsg: String) : CardState()
    data class WrongCardName(val errorMsg: String) : CardState()
}

