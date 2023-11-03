package com.example.eldarwallet.feature_card.domain.use_case

import android.content.res.Resources
import com.example.eldarwallet.R
import com.example.eldarwallet.feature_card.data.local.entity.CardEntity
import com.example.eldarwallet.feature_card.domain.model.CardDomainModel
import com.example.eldarwallet.feature_card.domain.repository.CardRepository
import com.example.eldarwallet.feature_card.utils.CardEvent
import com.example.eldarwallet.feature_card.utils.CryptoManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertCardUseCase @Inject constructor(
    private val resources: Resources,
    private val cardRepository: CardRepository,
    private val cryptoManager: CryptoManager
) {

    suspend operator fun invoke(
        cardToInsert: CardDomainModel
    ): Flow<CardEvent> = flow {
        try {
            val cardToInsertJson = Gson().toJson(cardToInsert, CardDomainModel::class.java)
            val encryptedCardToInsert = cryptoManager.encrypt(cardToInsertJson)
            val cardEntity = CardEntity(cardInfoEncrypted = encryptedCardToInsert)
            cardRepository.insertCard(cardEntity)
            emit(CardEvent.Success(cardToInsert))
        } catch (e: Exception) {
            val errorMsg = resources.getString(R.string.error_msg_unknown) + e.message
            emit(CardEvent.Error(errorMsg))
        }
    }
}