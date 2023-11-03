package com.example.eldarwallet.feature_pay_qr.data.repository

import com.example.eldarwallet.feature_pay_qr.data.remote.api.QRCodeApi
import com.example.eldarwallet.feature_pay_qr.domain.repository.QRCodeRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class QRCodeRepositoryImpl @Inject constructor(
    private val qrCodeApi: QRCodeApi
) : QRCodeRepository {

    override suspend fun getQRCode(userFullName: String): Response<ResponseBody> =
        qrCodeApi.postQRCode(userFullName).execute()

}

