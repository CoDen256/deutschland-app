package de.app.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import de.app.data.model.entities.AccountEntity
import de.app.data.model.entities.CredentialsEntity
import de.app.data.model.entities.CurrentLogin

@Database(
    entities = [
        AccountEntity::class,
        CredentialsEntity::class,
        CurrentLogin::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}