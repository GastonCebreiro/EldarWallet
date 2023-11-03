package com.example.eldarwallet.feature_card.data.local.entity


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "cards")
data class CardEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var cardInfoEncrypted: String

) : Parcelable

