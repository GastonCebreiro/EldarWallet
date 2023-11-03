package com.example.eldarwallet.feature_card.domain.repository

import com.example.eldarwallet.feature_card.data.local.entity.CardEntity
import kotlinx.coroutines.flow.Flow

interface CardRepository {

    fun getAllCards(): Flow<List<CardEntity>>
    suspend fun insertCard(card: CardEntity)
}