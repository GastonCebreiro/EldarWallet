package com.example.eldarwallet.feature_payment.di

import com.example.eldarwallet.core.data.AppDatabase
import com.example.eldarwallet.feature_payment.data.local.dao.PaymentDao
import com.example.eldarwallet.feature_payment.data.repository.PaymentRepositoryImpl
import com.example.eldarwallet.feature_payment.domain.repository.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PaymentModule {

    @Singleton
    @Provides
    fun providesPaymentDao(db: AppDatabase): PaymentDao = db.paymentDao()

    @Singleton
    @Provides
    fun providesPaymentRepository(paymentRepositoryImpl: PaymentRepositoryImpl): PaymentRepository =
        paymentRepositoryImpl
}