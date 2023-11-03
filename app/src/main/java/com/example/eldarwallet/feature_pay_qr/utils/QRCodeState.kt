package com.example.eldarwallet.feature_pay_qr.utils

import android.graphics.Bitmap

sealed class QRCodeState {
    object LoadingOn : QRCodeState()
    object LoadingOff : QRCodeState()
    data class Success(val qrBitmap: Bitmap) : QRCodeState()
    data class Error(val errorMsg: String) : QRCodeState()
}