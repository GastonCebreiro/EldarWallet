package com.example.eldarwallet.feature_pay_qr.data.remote.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface QRCodeApi {

    @FormUrlEncoded
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "X-RapidAPI-Key: 09a11f6f48msh6a78ce20703819ep12f099jsn6bff1856abc5",
        "X-RapidAPI-Host: neutrinoapi-qr-code.p.rapidapi.com"
    )
    @POST("qr-code")
    @Streaming
    fun postQRCode(@Field("content") content: String): Call<ResponseBody>
}