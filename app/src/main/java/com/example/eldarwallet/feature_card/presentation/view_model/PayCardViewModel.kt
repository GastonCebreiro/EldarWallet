package com.example.eldarwallet.feature_card.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.R
import com.example.eldarwallet.feature_card.domain.model.CardDomainModel
import com.example.eldarwallet.feature_card.utils.PayCardState
import com.example.eldarwallet.feature_login.domain.use_case.GetUserLoggedFullNameUseCase
import com.example.eldarwallet.feature_payment.utils.PaymentMethodType
import com.example.eldarwallet.feature_payment.domain.model.PaymentDomainModel
import com.example.eldarwallet.feature_payment.domain.use_case.InsertPaymentUseCase
import com.example.eldarwallet.feature_payment.utils.PaymentEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayCardViewModel @Inject constructor(
        private val resources: Resources,
        private val getUserLoggedFullNameUseCase: GetUserLoggedFullNameUseCase,
        private val insertPaymentUseCase: InsertPaymentUseCase
) : ViewModel() {

    private val _paymentState = MutableStateFlow<PayCardState>(PayCardState.LoadingOff)
    val paymentState: StateFlow<PayCardState> = _paymentState

    fun updateViewState(newState: PayCardState) {
        _paymentState.value = newState
    }

    fun generatePayment(card: CardDomainModel, amount: String) {
        _paymentState.value = PayCardState.LoadingOn
        val totalAmount = amount.toDoubleOrNull() ?: 0.0
        val userFullName = getUserLoggedFullNameUseCase.invoke()
        viewModelScope.launch(Dispatchers.IO) {
            val paymentToInsert = PaymentDomainModel(
                userFullName = userFullName,
                paymentMethod = PaymentMethodType.CARD.name,
                card = card,
                totalAmount = totalAmount
            )
            insertPaymentUseCase.invoke(paymentToInsert)
                .catch { e ->
                    val errorMsg =
                        resources.getString(R.string.error_msg_unknown) + e.message.toString()
                    _paymentState.value = PayCardState.Error(errorMsg)
                }
                .collect { res ->
                    when (res) {
                        is PaymentEvent.Success -> {
                            _paymentState.value = PayCardState.Success(res.payment)
                        }
                        is PaymentEvent.Error -> {
                            _paymentState.value = PayCardState.Error(res.errorMsg)
                        }
                    }
                }
        }

    }


}