package com.example.eldarwallet.feature_pay_qr.utils

import android.graphics.Bitmap

sealed class QRCodeEvent {
    data class Success(val bitmap: Bitmap) : QRCodeEvent()
    data class Error(val errorMsg: String) : QRCodeEvent()
}