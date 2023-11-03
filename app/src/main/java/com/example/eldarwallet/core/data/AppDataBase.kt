package com.example.eldarwallet.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eldarwallet.feature_card.data.local.dao.CardDao
import com.example.eldarwallet.feature_card.data.local.entity.CardEntity
import com.example.eldarwallet.feature_login.data.local.dao.UserDao
import com.example.eldarwallet.feature_login.data.local.entity.UserEntity
import com.example.eldarwallet.feature_payment.data.local.dao.PaymentDao
import com.example.eldarwallet.feature_payment.data.local.entity.PaymentEntity

@Database(entities = [UserEntity::class, CardEntity::class, PaymentEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun cardDao(): CardDao
    abstract fun paymentDao(): PaymentDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = buildDatabase(context)
            }
            return INSTANCE
        }

        private fun buildDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "myDB"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}