package com.example.eldarwallet.feature_payment.utils

import com.example.eldarwallet.R
import com.example.eldarwallet.feature_payment.domain.model.PaymentDomainModel

object PaymentUtils {

    fun getTotalAmountFromPaymentList(paymentList: List<PaymentDomainModel>): Double {
        return paymentList.sumOf { it.totalAmount }
    }

    fun getBalanceStatus(totalAmount: Double): BalanceStatus {
        return when {
            totalAmount > 0 -> BalanceStatus.POSITIVE
            totalAmount < 0 -> BalanceStatus.NEGATIVE
            else -> BalanceStatus.ZERO
        }
    }

    fun getBalanceColor(balanceStatus: BalanceStatus): Int {
        return when(balanceStatus) {
            BalanceStatus.POSITIVE -> R.color.green
            BalanceStatus.NEGATIVE -> R.color.red
            BalanceStatus.ZERO -> R.color.black
        }
    }
}