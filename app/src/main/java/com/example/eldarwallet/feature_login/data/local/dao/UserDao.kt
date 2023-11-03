package com.example.eldarwallet.feature_login.data.local.dao
import androidx.room.*
import com.example.eldarwallet.feature_login.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE name = :name AND lastName = :lastName")
    suspend fun getUser(name: String, lastName: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
}