package de.app.core.db

import androidx.room.*
import de.app.data.model.entities.AccountEntity
import de.app.data.model.entities.CredentialsEntity

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun getAll(): List<AccountEntity>

    @Query("SELECT * FROM account WHERE id = :id")
    fun getAccountById(id: String): AccountEntity?

    @Insert
    fun insert(account: AccountEntity)

    @Delete
    fun delete(account: AccountEntity)

    @Query("SELECT * FROM credentials WHERE accountId = :id")
    fun getCredentialsById(id: String): CredentialsEntity?

    @Insert
    fun insert(info: CredentialsEntity)

    @Delete
    fun delete(info: CredentialsEntity)

    @Transaction
    fun insert(account: AccountEntity, info: CredentialsEntity){
        insert(account)
        insert(info)
    }

    @Transaction
    fun delete(account: AccountEntity, info: CredentialsEntity){
        delete(info)
        delete(account)
    }
}