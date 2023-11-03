package com.example.eldarwallet.feature_card.di

import com.example.eldarwallet.core.data.AppDatabase
import com.example.eldarwallet.feature_card.data.local.dao.CardDao
import com.example.eldarwallet.feature_card.data.repository.CardRepositoryImpl
import com.example.eldarwallet.feature_card.domain.repository.CardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CardModule {

    @Singleton
    @Provides
    fun providesCardDao(db: AppDatabase): CardDao = db.cardDao()

    @Singleton
    @Provides
    fun providesCardRepository(cardRepositoryImpl: CardRepositoryImpl): CardRepository =
        cardRepositoryImpl
}