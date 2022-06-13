package de.app.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun getAll(): List<AccountEntity>

    @Insert
    fun insert(user: AccountEntity)

    @Delete
    fun delete(user: AccountEntity)
}