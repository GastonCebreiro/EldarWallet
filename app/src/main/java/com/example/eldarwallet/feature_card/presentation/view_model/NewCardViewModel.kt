package com.example.eldarwallet.feature_card.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.R
import com.example.eldarwallet.feature_card.domain.model.CardDomainModel
import com.example.eldarwallet.feature_card.domain.use_case.InsertCardUseCase
import com.example.eldarwallet.feature_card.utils.*
import com.example.eldarwallet.feature_login.domain.use_case.GetUserLoggedFullNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewCardViewModel @Inject constructor(
    private val resources: Resources,
    private val insertCardUseCase: InsertCardUseCase,
    private val getUserLoggedFullNameUseCase: GetUserLoggedFullNameUseCase
) : ViewModel() {

    private val _formattedCardNumber = MutableStateFlow("")
    val formattedCardNumber: StateFlow<String> = _formattedCardNumber

    private val _formattedCardExpireDate = MutableStateFlow("")
    val formattedCardExpireDate: StateFlow<String> = _formattedCardExpireDate

    private val _cardType = MutableStateFlow<CardTypeState>(CardTypeState.None)
    val cardType: StateFlow<CardTypeState> = _cardType

    private val _cardRegister = MutableStateFlow<CardState>(CardState.LoadingOff)
    val cardRegister: StateFlow<CardState> = _cardRegister


    fun updateViewState(newState: CardState) {
        _cardRegister.value = newState
    }

    fun setCardNumberFormat(text: String) {
        _cardType.value = CardTypeState.None
        val formattedText = CardUtils.setCardNumberFormat(text)
        _formattedCardNumber.value = formattedText
        when (CardUtils.getCardType(formattedText)) {
            CardType.VISA -> _cardType.value = CardTypeState.Visa
            CardType.MASTERCARD -> _cardType.value = CardTypeState.MasterCard
            CardType.AMEX -> _cardType.value = CardTypeState.AmericanExpress
            CardType.OTHER -> _cardType.value = CardTypeState.None
        }
    }

    fun setCardExpireDateFormat(text: String) {
        val formattedText = CardUtils.setCardExpireDateFormat(text)
        _formattedCardExpireDate.value = formattedText
    }

    fun checkCardToRegister(
        cardNumber: String,
        cardCode: String,
        cardExpireDate: String,
        cardFullName: String
    ) {
        _cardRegister.value = CardState.LoadingOn
        val cardType = CardUtils.getCardType(cardNumber)
        val cardNumberFormatted = cardNumber.trim().replace(" ", "")
        val cardCodeFormatted = cardCode.trim()
        val cardExpireMonth = cardExpireDate.substringBefore("/")
        val cardExpireYear = cardExpireDate.substringAfter("/").trim()
        val cardFullNameFormatted = cardFullName.trim()
        if (cardNumberFormatted.isBlank()) {
            _cardRegister.value =
                CardState.WrongCardNumber(resources.getString(R.string.error_msg_enter_card_number))
            return
        }
        when (cardType) {
            CardType.VISA -> {
                if (cardNumberFormatted.length != VISA_CARD_NUMBER_LENGTH) {
                    _cardRegister.value =
                        CardState.WrongCardNumber(resources.getString(R.string.error_msg_enter_card_number))
                    return
                }
                if (cardCodeFormatted.length != VISA_CARD_CODE_LENGTH) {
                    _cardRegister.value =
                        CardState.WrongCardCode(resources.getString(R.string.error_msg_enter_card_code))
                    return
                }
            }
            CardType.MASTERCARD -> {
                if (cardNumberFormatted.length != MASTER_CARD_NUMBER_LENGTH) {
                    _cardRegister.value =
                        CardState.WrongCardNumber(resources.getString(R.string.error_msg_enter_card_number))
                    return
                }
                if (cardCodeFormatted.length != MASTER_CARD_CODE_LENGTH) {
                    _cardRegister.value =
                        CardState.WrongCardCode(resources.getString(R.string.error_msg_enter_card_code))
                    return
                }
            }
            CardType.AMEX -> {
                if (cardNumberFormatted.length != AMEX_CARD_NUMBER_LENGTH) {
                    _cardRegister.value =
                        CardState.WrongCardNumber(resources.getString(R.string.error_msg_enter_card_number))
                    return
                }
                if (cardCodeFormatted.length != AMEX_CARD_CODE_LENGTH) {
                    _cardRegister.value =
                        CardState.WrongCardCode(resources.getString(R.string.error_msg_enter_card_code))
                    return
                }
            }
            CardType.OTHER -> {
                _cardRegister.value =
                    CardState.Error(resources.getString(R.string.error_msg_card_unknown))
                return
            }
        }
        if (cardCodeFormatted.isBlank()) {
            _cardRegister.value =
                CardState.WrongCardCode(resources.getString(R.string.error_msg_enter_card_code))
            return
        }
        if (cardExpireYear.length != 2 || cardExpireMonth.length != 2 || cardExpireMonth.toIntOrNull() !in 1..12) {
            _cardRegister.value =
                CardState.WrongCardDate(resources.getString(R.string.error_msg_enter_card_date))
            return
        }
        if (cardFullNameFormatted.isBlank()) {
            _cardRegister.value =
                CardState.WrongCardName(resources.getString(R.string.error_msg_enter_card_name))
            return
        }
        val isValidCardFullName = CardUtils.isValidCardFullNameWithUser(
            userFullName = getUserLoggedFullNameUseCase.invoke(),
            cardFullName = cardFullNameFormatted
        )
        if (!isValidCardFullName) {
            _cardRegister.value =
                CardState.WrongCardName(resources.getString(R.string.error_msg_invalid_card_name))
            return
        }

        insertCard(
            cardNumberFormatted,
            cardCodeFormatted,
            cardExpireMonth,
            cardExpireYear,
            cardFullName,
            cardType.name
        )
    }

    private fun insertCard(
        cardNumber: String,
        cardCode: String,
        cardExpireMonth: String,
        cardExpireYear: String,
        cardFullName: String,
        cardType: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val cardToInsert = CardDomainModel(
                cardNumber = cardNumber,
                cardCode = cardCode,
                expireDate = CardUtils.getCardExpireDateToInsert(cardExpireMonth, cardExpireYear),
                fullName = cardFullName,
                cardType = cardType
            )
            insertCardUseCase.invoke(cardToInsert)
                .catch { e ->
                    val errorMsg =
                        resources.getString(R.string.error_msg_unknown) + e.message.toString()
                    _cardRegister.value = CardState.Error(errorMsg)
                }
                .collect { res ->
                    when (res) {
                        is CardEvent.Success -> {
                            _cardRegister.value = CardState.Success(res.card)
                        }
                        is CardEvent.Error -> {
                            _cardRegister.value = CardState.Error(res.errorMsg)
                        }
                    }
                }
        }
    }

    companion object {
        const val VISA_CARD_NUMBER_LENGTH = 16
        const val MASTER_CARD_NUMBER_LENGTH = 16
        const val AMEX_CARD_NUMBER_LENGTH = 15

        const val VISA_CARD_CODE_LENGTH = 3
        const val MASTER_CARD_CODE_LENGTH = 3
        const val AMEX_CARD_CODE_LENGTH = 4
    }

}