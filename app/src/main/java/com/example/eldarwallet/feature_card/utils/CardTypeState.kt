package com.example.eldarwallet.feature_card.utils

sealed class CardTypeState {
    object None : CardTypeState()
    object Visa : CardTypeState()
    object MasterCard : CardTypeState()
    object AmericanExpress : CardTypeState()
}

