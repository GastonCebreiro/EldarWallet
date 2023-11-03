package com.example.eldarwallet.feature_main.utils

import com.example.eldarwallet.feature_payment.utils.BalanceStatus

sealed class BalanceState {
    object LoadingOn : BalanceState()
    object LoadingOff : BalanceState()
    data class Success(val balanceAmount: Double, val balanceStatus: BalanceStatus) : BalanceState()
    data class Error(val errorMsg: String) : BalanceState()
}

