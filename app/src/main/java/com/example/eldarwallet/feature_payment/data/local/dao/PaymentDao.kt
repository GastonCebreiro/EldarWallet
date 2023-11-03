package com.example.eldarwallet.feature_payment.data.local.dao
import androidx.room.*
import com.example.eldarwallet.feature_payment.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    @Query("SELECT * FROM payments WHERE userFullName = :userFullName")
    fun getPaymentsByUser(userFullName: String): Flow<List<PaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity)
}