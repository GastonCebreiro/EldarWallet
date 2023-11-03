package com.example.eldarwallet.feature_card.utils

import com.example.eldarwallet.R
import com.example.eldarwallet.feature_card.domain.model.CardDomainModel

object CardUtils {


    fun getCardType(cardNumber: String): CardType {
        if (cardNumber.isEmpty()) return CardType.OTHER

        return when (cardNumber[0].toString().toInt()) {
            5 -> CardType.MASTERCARD
            4 -> CardType.VISA
            3 -> CardType.AMEX
            else -> CardType.OTHER
        }

    }

    fun setCardNumberFormat(text: String): String {
        val strippedText = text.replace(" ", "")

        if (strippedText.isNotEmpty()) {
            val formattedText = StringBuilder()
            val firstChar = strippedText[0]
            if (firstChar == '3') { // AMERICAN EXPRESS
                for (i in strippedText.indices) {
                    when (i) {
                        4, 10 -> formattedText.append(' ')
                    }
                    formattedText.append(strippedText[i])
                }
            } else {
                for (i in strippedText.indices) {
                    when (i) {
                        4, 8, 12 -> formattedText.append(' ')
                    }
                    formattedText.append(strippedText[i])
                }
            }
            return formattedText.toString()
        }
        return ""
    }

    fun setCardExpireDateFormat(text: String): String {
        if (text.length == 2 && !text.contains("/")) {
            return "$text/"
        }
        return text
    }

    fun getCardExpireDateToInsert(cardExpireMonth: String, cardExpireYear: String): String {
        return "$cardExpireMonth/$cardExpireYear"
    }

    fun isValidCardFullNameWithUser(userFullName: String, cardFullName: String): Boolean {
        return userFullName.isNotEmpty() && userFullName.equals(cardFullName, ignoreCase = true)
    }

    fun getCardNumberFormatted(card: CardDomainModel): String {
        val cardNumber = card.cardNumber
        if (cardNumber.isEmpty()) return ""
        return when (card.cardType) {
            CardType.VISA.name,
            CardType.MASTERCARD.name -> {
                cardNumber.chunked(4).joinToString(" ")
            }
            CardType.AMEX.name -> {
                "${cardNumber.take(4)} ${cardNumber.drop(4).take(6)} ${cardNumber.drop(10)}"
            }
            CardType.OTHER.name -> {
                ""
            }
            else -> ""
        }
    }

    fun getResIdByCardType(card: CardDomainModel): Int {
        return when (card.cardType) {
            CardType.VISA.name -> {
                R.drawable.visa_logo
            }
            CardType.MASTERCARD.name -> {
                R.drawable.mastercard_logo
            }
            CardType.AMEX.name -> {
                R.drawable.amex_logo
            }
            CardType.OTHER.name -> {
                0
            }
            else -> 0
        }
    }

    fun getCardColor(card: CardDomainModel): Int {
        return when(card.cardType) {
            CardType.VISA.name -> R.color.card_light_blue
            CardType.MASTERCARD.name -> R.color.card_silver
            CardType.AMEX.name -> R.color.card_gold
            else -> R.color.white
        }
    }
}