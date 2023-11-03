package com.example.eldarwallet.feature_main.utils

import com.example.eldarwallet.feature_card.domain.model.CardDomainModel

sealed class CardListState {
    object LoadingOn : CardListState()
    object LoadingOff : CardListState()
    data class Success(val cardList: List<CardDomainModel>) : CardListState()
    data class Error(val errorMsg: String) : CardListState()
}

