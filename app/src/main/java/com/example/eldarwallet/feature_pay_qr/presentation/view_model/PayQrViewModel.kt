package com.example.eldarwallet.feature_pay_qr.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.R
import com.example.eldarwallet.feature_login.domain.use_case.GetUserLoggedFullNameUseCase
import com.example.eldarwallet.feature_pay_qr.domain.use_case.GetQRCodeUseCase
import com.example.eldarwallet.feature_pay_qr.utils.QRCodeEvent
import com.example.eldarwallet.feature_pay_qr.utils.QRCodeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayQrViewModel @Inject constructor(
    private val resources: Resources,
    private val getQRCodeUseCase: GetQRCodeUseCase,
    private val getUserLoggedFullNameUseCase: GetUserLoggedFullNameUseCase
) : ViewModel() {

    private val _qrCodeState = MutableStateFlow<QRCodeState>(QRCodeState.LoadingOff)
    val qrCodeState: StateFlow<QRCodeState> = _qrCodeState

    fun updateViewState(newState: QRCodeState) {
        _qrCodeState.value = newState
    }

    fun getQRCodeBitmap() {
        viewModelScope.launch(Dispatchers.IO) {
            _qrCodeState.value = QRCodeState.LoadingOn
            val userFullName = getUserLoggedFullNameUseCase.invoke()
            getQRCodeUseCase.invoke(userFullName)
                .catch { e ->
                    val errorMsg =
                        resources.getString(R.string.error_msg_unknown) + e.message.toString()
                    _qrCodeState.value = QRCodeState.Error(errorMsg)
                }
                .collect { qrCodeEvent ->
                    when (qrCodeEvent) {
                        is QRCodeEvent.Success -> {
                            _qrCodeState.value = QRCodeState.Success(qrCodeEvent.bitmap)
                        }
                        is QRCodeEvent.Error -> {
                            _qrCodeState.value = QRCodeState.Error(qrCodeEvent.errorMsg)
                        }
                    }
                }
        }
    }
}

