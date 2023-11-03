package com.example.eldarwallet.feature_card.domain.use_case

import com.example.eldarwallet.feature_card.domain.model.CardDomainModel
import com.example.eldarwallet.feature_card.domain.repository.CardRepository
import com.example.eldarwallet.feature_card.utils.CryptoManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCardsByUserUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val cryptoManager: CryptoManager
) {

    operator fun invoke(userFullName: String): Flow<List<CardDomainModel>> {
        val allCards = cardRepository.getAllCards().map { cardList ->
            cardList.map { card ->
                val encryptedCardInfo = card.cardInfoEncrypted
                val decryptedCard = cryptoManager.decrypt(encryptedCardInfo)
                Gson().fromJson(decryptedCard, CardDomainModel::class.java)
            }
        }
        val allCardsByUser = allCards.map { cardList ->
            cardList.filter { userFullName.equals(it.fullName, ignoreCase = true) }
        }
        return allCardsByUser
    }
}