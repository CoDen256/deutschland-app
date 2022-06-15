package de.app.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import de.app.data.model.entities.AccountEntity
import de.app.data.model.entities.CredentialsEntity

@Database(
    entities = [
        AccountEntity::class,
        CredentialsEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}