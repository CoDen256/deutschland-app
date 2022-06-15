package de.app.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val city: String,
    val country: String,
    val postalCode: String,
    val type: String,
)