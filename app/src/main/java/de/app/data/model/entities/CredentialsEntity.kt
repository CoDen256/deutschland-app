package de.app.data.model.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "credentials",
    foreignKeys = [ForeignKey(
        entity = AccountEntity::class,
        parentColumns = ["id"],
        childColumns = ["accountId"]
    )]
)
class CredentialsEntity(
    @PrimaryKey val accountId: String,
    val pin: String,
)