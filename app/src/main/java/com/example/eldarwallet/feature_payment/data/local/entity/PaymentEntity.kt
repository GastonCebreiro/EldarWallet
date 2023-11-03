package com.example.eldarwallet.feature_payment.data.local.entity


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.eldarwallet.feature_card.domain.model.CardDomainModel
import com.example.eldarwallet.feature_payment.domain.model.PaymentDomainModel
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "payments")
data class PaymentEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var userFullName: String?,
    var paymentMethod: String?,
    var card: String?,
    var totalAmount: Double?

) : Parcelable

fun PaymentEntity.toDomainModel() = PaymentDomainModel(
    userFullName = this.userFullName.orEmpty(),
    paymentMethod = this.paymentMethod.orEmpty(),
    card = Gson().fromJson(this.card, CardDomainModel::class.java),
    totalAmount = totalAmount ?: 0.0
)

fun List<PaymentEntity>.toDomainModelList(): List<PaymentDomainModel> {
    return this.map { it.toDomainModel() }
}

