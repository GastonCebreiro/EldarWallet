package com.example.eldarwallet.feature_pay_qr.domain.use_case

import android.graphics.BitmapFactory
import com.example.eldarwallet.feature_pay_qr.domain.repository.QRCodeRepository
import com.example.eldarwallet.feature_pay_qr.utils.QRCodeEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetQRCodeUseCase @Inject constructor(
    private val qrCodeRepository: QRCodeRepository
) {

    suspend operator fun invoke(userFullName: String): Flow<QRCodeEvent> = flow {
        try {
            val response = qrCodeRepository.getQRCode(userFullName)

            if (response.isSuccessful) {
                val inputStream = response.body()?.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                emit(QRCodeEvent.Success(bitmap))
            } else {
                emit(QRCodeEvent.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(QRCodeEvent.Error(e.message.orEmpty()))
        }
    }
}