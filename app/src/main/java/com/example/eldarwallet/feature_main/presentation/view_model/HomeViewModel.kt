package com.example.eldarwallet.feature_main.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.R
import com.example.eldarwallet.feature_card.domain.use_case.GetCardsByUserUseCase
import com.example.eldarwallet.feature_login.domain.use_case.GetUserLoggedFullNameUseCase
import com.example.eldarwallet.feature_main.domain.use_case.GetPaymentsByUserUseCase
import com.example.eldarwallet.feature_main.utils.BalanceState
import com.example.eldarwallet.feature_main.utils.CardListState
import com.example.eldarwallet.feature_payment.utils.PaymentUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val resources: Resources,
    private val getCardsByUserUseCase: GetCardsByUserUseCase,
    private val getPaymentsByUserUseCase: GetPaymentsByUserUseCase,
    private val getUserLoggedFullNameUseCase: GetUserLoggedFullNameUseCase
) : ViewModel() {

    private val _cardState = MutableStateFlow<CardListState>(CardListState.LoadingOff)
    val cardState: StateFlow<CardListState> = _cardState

    private val _balanceState = MutableStateFlow<BalanceState>(BalanceState.LoadingOff)
    val balanceState: StateFlow<BalanceState> = _balanceState


    fun getCardsByUser() {
        viewModelScope.launch {
            _cardState.value = CardListState.LoadingOn
            val userFullName = getUserLoggedFullNameUseCase.invoke()
            getCardsByUserUseCase.invoke(userFullName)
                .catch { e ->
                    val errorMsg = resources.getString(R.string.error_msg_unknown) + e.message
                    _cardState.value = CardListState.Error(errorMsg)
                }
                .collect { cardList ->
                    _cardState.value = CardListState.Success(cardList)
                }
        }
    }

    fun getBalanceByUser() {
        viewModelScope.launch {
            _balanceState.value = BalanceState.LoadingOn
            val userFullName = getUserLoggedFullNameUseCase.invoke()
            getPaymentsByUserUseCase.invoke(userFullName)
                .catch { e ->
                    val errorMsg = resources.getString(R.string.error_msg_unknown) + e.message
                    _balanceState.value = BalanceState.Error(errorMsg)
                }
                .collect { paymentList ->
                    val totalAmountByUser = PaymentUtils.getTotalAmountFromPaymentList(paymentList)
                    val balanceAmount = if (totalAmountByUser != 0.0) -totalAmountByUser else 0.0
                    val balanceStatus = PaymentUtils.getBalanceStatus(balanceAmount)
                    _balanceState.value = BalanceState.Success(balanceAmount, balanceStatus)
                }
        }
    }

    fun updateCardState(newState: CardListState) {
        _cardState.value = newState
    }

    fun updateBalanceState(newState: BalanceState) {
        _balanceState.value = newState
    }
}