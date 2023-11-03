package com.example.eldarwallet.feature_main.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eldarwallet.databinding.ItemCardBinding
import com.example.eldarwallet.feature_card.domain.model.CardDomainModel
import com.example.eldarwallet.feature_card.utils.CardUtils

class CardAdapter(
    private val cardList: List<CardDomainModel>,
    private val onClick: (CardDomainModel) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    inner class CardHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedCard = cardList[position]
                    onClick(clickedCard)
                }
            }
        }

        fun bind(card: CardDomainModel) {
            binding.ivCardLogo.setImageResource(CardUtils.getResIdByCardType(card))
            binding.tvCardNumber.text = CardUtils.getCardNumberFormatted(card)
            binding.tvExpireDate.text = card.expireDate
            binding.clCard.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    CardUtils.getCardColor(card)
                )
            )
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardHolder(binding)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val card = cardList[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

}