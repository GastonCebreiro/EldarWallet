package com.example.eldarwallet.feature_pay_qr.domain.repository

import com.example.eldarwallet.feature_card.data.local.entity.CardEntity
import com.example.eldarwallet.feature_card.domain.model.CardDomainModel
import com.example.eldarwallet.feature_pay_qr.utils.QRCodeEvent
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface QRCodeRepository {

    suspend fun getQRCode(userFullName: String): Response<ResponseBody>

}