package de.app.data.model.entities

import androidx.room.*
import de.app.data.model.entities.AccountEntity
import de.app.data.model.entities.CredentialsEntity
import de.app.data.model.entities.CurrentLogin

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    suspend fun getAll(): List<AccountEntity>

    @Query("SELECT * FROM account WHERE id = :id")
    suspend fun getAccountById(id: String): AccountEntity?

    @Insert
    suspend fun insert(account: AccountEntity)

    @Delete
    suspend fun delete(account: AccountEntity)

    @Query("SELECT * FROM credentials WHERE accountId = :id")
    suspend fun getCredentialsById(id: String): CredentialsEntity?

    @Insert
    suspend fun insert(info: CredentialsEntity)

    @Delete
    suspend fun delete(info: CredentialsEntity)

    @Transaction
    suspend fun insert(account: AccountEntity, info: CredentialsEntity){
        insert(account)
        insert(info)
    }

    @Transaction
    suspend fun delete(account: AccountEntity, info: CredentialsEntity){
        delete(info)
        delete(account)
    }

    @Transaction
    suspend fun delete(account: AccountEntity, info: CredentialsEntity, currentLogin: CurrentLogin){
        delete(info)
        delete(account)
        delete(currentLogin)
    }

    @Query("SELECT * FROM login LIMIT 1")
    suspend fun getCurrentLogin(): List<CurrentLogin>

    @Insert
    suspend fun insert(login: CurrentLogin)

    @Delete
    suspend fun delete(login: CurrentLogin)

    @Transaction
    suspend fun setCurrentLogin(oldLogin: CurrentLogin, newLogin: CurrentLogin){
        delete(oldLogin)
        insert(newLogin)
    }
}

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val city: String,
    val country: String,
    val postalCode: String,
    val type: String,
)

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

@Entity(
    tableName = "login",
    foreignKeys = [ForeignKey(
        entity = AccountEntity::class,
        parentColumns = ["id"],
        childColumns = ["accountId"]
    )]
)
data class CurrentLogin (
    @PrimaryKey val accountId: String
)