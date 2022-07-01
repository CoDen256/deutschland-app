package de.app.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import de.app.data.model.entities.UserDao
import de.app.data.model.entities.UserEntity
import de.app.data.model.entities.UserCredentials
import de.app.data.model.entities.CurrentUser

@Database(
    entities = [
        UserEntity::class,
        UserCredentials::class,
        CurrentUser::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): UserDao
}