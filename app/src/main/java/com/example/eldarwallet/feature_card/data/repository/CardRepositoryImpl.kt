package com.example.eldarwallet.feature_card.data.repository

import com.example.eldarwallet.feature_card.data.local.entity.CardEntity
import com.example.eldarwallet.feature_card.data.local.dao.CardDao
import com.example.eldarwallet.feature_card.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardDao: CardDao
): CardRepository {

    override fun getAllCards(): Flow<List<CardEntity>> =
        cardDao.getAllCards()

    override suspend fun insertCard(
        card: CardEntity
    ) {
        cardDao.insertCard(card)
    }

}

