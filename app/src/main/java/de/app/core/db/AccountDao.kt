package de.app.core.db

import androidx.room.*
import de.app.data.model.entities.AccountEntity
import de.app.data.model.entities.CredentialsEntity
import de.app.data.model.entities.CurrentLogin

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    suspend fun getAll(): List<AccountEntity>

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

    @Transaction
    fun delete(account: AccountEntity, info: CredentialsEntity, currentLogin: CurrentLogin){
        delete(info)
        delete(account)
        delete(currentLogin)
    }

    @Query("SELECT * FROM login LIMIT 1")
    fun getCurrentLogin(): List<CurrentLogin>

    @Insert
    fun insert(login: CurrentLogin)

    @Delete
    fun delete(login: CurrentLogin)

    @Transaction
    fun setCurrentLogin(oldLogin: CurrentLogin, newLogin: CurrentLogin){
        delete(oldLogin)
        insert(newLogin)
    }
}