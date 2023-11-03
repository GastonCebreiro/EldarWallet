package com.example.eldarwallet.feature_login.data.local.entity


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.eldarwallet.feature_login.domain.model.UserDomainModel
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "users")
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String?,
    var lastName: String?,
    var password: String?

) : Parcelable

fun UserEntity.toDomainModel() = UserDomainModel(
    name = this.name.orEmpty(),
    lastName = this.lastName.orEmpty(),
    password = this.password.orEmpty()
)