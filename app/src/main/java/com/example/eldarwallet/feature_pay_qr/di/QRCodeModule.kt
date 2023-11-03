package com.example.eldarwallet.feature_pay_qr.di

import com.example.eldarwallet.core.data.AppDatabase
import com.example.eldarwallet.feature_card.data.local.dao.CardDao
import com.example.eldarwallet.feature_card.data.repository.CardRepositoryImpl
import com.example.eldarwallet.feature_card.domain.repository.CardRepository
import com.example.eldarwallet.feature_pay_qr.data.repository.QRCodeRepositoryImpl
import com.example.eldarwallet.feature_pay_qr.domain.repository.QRCodeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class QRCodeModule {

    @Singleton
    @Provides
    fun providesQRCodeRepository(qrCodeRepositoryImpl: QRCodeRepositoryImpl): QRCodeRepository =
        qrCodeRepositoryImpl
}